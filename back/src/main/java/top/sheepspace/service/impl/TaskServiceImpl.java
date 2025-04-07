package top.sheepspace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.sheepspace.controller.Code;
import top.sheepspace.exception.BusinessException;
import top.sheepspace.pojo.MyPriorityQueue;
import top.sheepspace.pojo.Task;
import top.sheepspace.dao.TaskDao;
import top.sheepspace.service.IOpenAiService;
import top.sheepspace.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sheepspace.utils.Tools;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Sheep
 * @since 2024-04-22
 */
@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements ITaskService {
    @Autowired
    TaskDao taskDao;

    @Autowired
    IOpenAiService openAiService;

    @Override
    public boolean save(Task task) {
        Integer parentId = task.getParentId();
        // 获取同级任务列表并按sequence排序
        List<Task> siblings = getSiblingsOrderBySequence(parentId);
        
        // 如果没有指定sequence，则添加到最前面
        if (task.getSequence() == null) {
            task.setSequence(siblings.isEmpty() ? 
                Task.SEQUENCE_STEP : 
                siblings.get(0).getSequence() - Task.SEQUENCE_STEP);
        }
        
        // 如果sequence小于最小值，需要重平衡
        if (task.getSequence() <= Task.SEQUENCE_STEP) {
            rebalanceSequences(parentId);
            // 重新获取同级任务
            siblings = getSiblingsOrderBySequence(parentId);
            // 在重平衡后，将新任务放在最前面
            task.setSequence(siblings.isEmpty() ? 
                Task.SEQUENCE_STEP : 
                siblings.get(0).getSequence() - Task.SEQUENCE_STEP);
        }
        
        // 设置默认值
        task.setVersion(0);
        task.setStartTime(new Date());
        // 新建任务默认未完成
        if (task.getBeDeleted() == null) {
            task.setBeDeleted(false);
        }
        
        return super.save(task);
    }

    /**
     * 获取同级任务列表并按sequence排序
     */
    private List<Task> getSiblingsOrderBySequence(Integer parentId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        if (parentId == null) {
            wrapper.isNull(Task::getParentId);
        } else {
            wrapper.eq(Task::getParentId, parentId);
        }
        wrapper.orderByAsc(Task::getSequence);
        return taskDao.selectList(wrapper);
    }

    /**
     * 计算任务的基准序号
     * 根任务和子任务使用不同的序号范围以避免冲突
     */
    private int calculateBaseSequence(Integer parentId, int taskIndex, boolean isFinished) {
        // 使用更大的基数来避免序号重叠
        final int BASE_MULTIPLIER = 1_000_000;  // 100万
        final int TASK_STEP = 100_000;          // 10万
        
        int baseValue;
        if (parentId == null) {
            // 根任务使用 1,000,000 到 100,000,000 的序号范围
            baseValue = BASE_MULTIPLIER;
            if (isFinished) {
                baseValue += 50_000_000;  // 已完成的根任务从5千万开始
            }
        } else {
            // 子任务使用 100,000,000 + parentId * 1,000,000 的序号范围
            baseValue = 100_000_000 + (parentId * BASE_MULTIPLIER);
            if (isFinished) {
                baseValue += 500_000;  // 已完成的子任务偏移50万
            }
        }
        
        return baseValue + (taskIndex * TASK_STEP);
    }

    /**
     * 重新平衡指定父任务下所有子任务的sequence
     */
    private void rebalanceSequences(Integer parentId) {
        List<Task> tasks = getSiblingsOrderBySequence(parentId);
        if (tasks.isEmpty()) {
            return;
        }
        
        try {
            List<Task> unfinishedTasks = new ArrayList<>();
            List<Task> finishedTasks = new ArrayList<>();
            
            for (Task task : tasks) {
                Task freshTask = getById(task.getId());
                if (freshTask == null) continue;
                if (freshTask.getBeDeleted()) {
                    finishedTasks.add(freshTask);
                } else {
                    unfinishedTasks.add(freshTask);
                }
            }
            
            unfinishedTasks.sort(Comparator.comparing(Task::getSequence));
            finishedTasks.sort(Comparator.comparing(Task::getSequence));
            
            int baseSequence = parentId == null ? 1_000_000 : 100_000;
            int step = parentId == null ? 100_000 : 10_000;
            
            int currentSequence = baseSequence;
            for (Task task : unfinishedTasks) {
                updateTaskSequenceIfNeeded(task, currentSequence);
                currentSequence += step;
            }
            
            int finishedBaseOffset = parentId == null ? 50_000_000 : 500_000;
            currentSequence = baseSequence + finishedBaseOffset;
            
            for (Task task : finishedTasks) {
                updateTaskSequenceIfNeeded(task, currentSequence);
                currentSequence += step;
            }
            
            List<Task> updatedTasks = getSiblingsOrderBySequence(parentId);
            Set<Integer> sequences = new HashSet<>();
            Map<Integer, List<Integer>> duplicateSequences = new HashMap<>();
            
            for (Task task : updatedTasks) {
                if (!sequences.add(task.getSequence())) {
                    duplicateSequences
                        .computeIfAbsent(task.getSequence(), k -> new ArrayList<>())
                        .add(task.getId());
                }
            }
            
            if (!duplicateSequences.isEmpty()) {
                String duplicateInfo = duplicateSequences.entrySet().stream()
                    .map(e -> String.format("sequence %d: tasks %s", 
                        e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", "));
                log.error("Duplicate sequences found: {}", duplicateInfo);
                throw new BusinessException(Code.UPDATE_ERR, 
                    "重新平衡任务序号失败：存在重复序号 - " + duplicateInfo);
            }
        } catch (Exception e) {
            log.error("Failed to rebalance sequences for parent {}: {}", 
                parentId, e.getMessage());
            throw new BusinessException(Code.UPDATE_ERR, 
                "重新平衡任务序号失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新任务序号，使用乐观锁并支持重试
     */
    private void updateTaskSequence(Task task, int newSequence) {
        boolean updated = false;
        int retryCount = 0;
        Task freshTask = null;
        
        while (!updated && retryCount < 3) {
            try {
                // 每次尝试前先获取最新的任务状态
                freshTask = getById(task.getId());
                if (freshTask == null) {
                    log.error("Task {} not found during sequence update", task.getId());
                    throw new BusinessException(Code.TASK_NOT_FOUND, "任务不存在");
                }
                
                log.debug("Updating task {} sequence: current version={}, sequence={}, new sequence={}", 
                    task.getId(), freshTask.getVersion(), freshTask.getSequence(), newSequence);
                
                // 创建新的更新包装器
                UpdateWrapper<Task> updateWrapper = new UpdateWrapper<Task>()
                    .eq("id", task.getId())
                    .eq("version", freshTask.getVersion())
                    .set("sequence", newSequence)
                    .set("version", freshTask.getVersion() + 1);
                
                // 创建新的任务对象进行更新
                Task updateTask = new Task();
                updateTask.setId(task.getId());
                updateTask.setVersion(freshTask.getVersion());
                updateTask.setSequence(newSequence);
                
                updated = update(updateTask, updateWrapper);
                
                if (updated) {
                    // 更新成功后，同步更新原始任务对象的状态
                    task.setVersion(freshTask.getVersion() + 1);
                    task.setSequence(newSequence);
                    log.debug("Successfully updated task {} sequence to {} (version {})", 
                        task.getId(), newSequence, task.getVersion());
                } else {
                    log.warn("Failed to update task {} sequence: version conflict (current version={})", 
                        task.getId(), freshTask.getVersion());
                }
            } catch (Exception e) {
                log.warn("Error updating task {} sequence on attempt {}: {}", 
                    task.getId(), retryCount + 1, e.getMessage());
            }
            
            if (!updated) {
                retryCount++;
                if (retryCount < 3) {
                    log.debug("Retrying sequence update for task {} (attempt {}/3)", task.getId(), retryCount + 1);
                    try {
                        Thread.sleep(100 * retryCount); // 递增延迟时间
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new BusinessException(Code.UPDATE_ERR, "更新任务序号被中断");
                    }
                }
            }
        }
        
        if (!updated) {
            String currentState = freshTask != null ? 
                String.format("(current version=%d, sequence=%d)", 
                    freshTask.getVersion(), freshTask.getSequence()) : 
                "(task not found)";
            
            log.error("Failed to update sequence for task {} after {} attempts {}", 
                task.getId(), retryCount, currentState);
            throw new BusinessException(Code.UPDATE_ERR, 
                String.format("更新任务 %d 序号失败（%s），请重试", 
                    task.getId(), currentState));
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        Task beDeletedTask = taskDao.selectById(id);
        // 将被删除节点后的所有任务的sequence前移一位
        taskDao.moveForward(beDeletedTask.getParentId(), beDeletedTask.getSequence());
        return super.removeById(id);
    }

    @Override
    public boolean splitSubTasks(Integer parentId) {
        String description = taskDao.selectById(parentId).getDescription();
        Integer sequence = getSameLevelTaskCountExceptSelf(parentId);
        List<String> subTasksDesc = openAiService.getSubTasksDesc(description);
        Task subTask;
        for (String taskDes : subTasksDesc) {
            subTask = new Task();
            // 设置父任务id
            subTask.setParentId(parentId);
            // 设置任务内容
            subTask.setDescription(taskDes);
            // 设置任务开始时间
            subTask.setStartTime(Tools.getNowDateTime());
            // 设置乐观锁版本后，不设置会报错
            subTask.setVersion(0);
            // 设置执行顺序
            subTask.setSequence(sequence++);
            // 保存任务
            super.save(subTask);
        }
        return true;
    }

    @Override
    public boolean saveBatch(Collection<Task> entityList) {
        Integer parentId = null;
        Integer sequence = 0;
        Date now = new Date();
        for (Task task : entityList) {
            if (null == parentId) {
                // 父节点id，这样做是因为不方便直接获取集合第一个元素，这个if是一定会执行的
                parentId = task.getParentId();
                // 获得当前同层级任务数，作为第一个被加入任务的顺序，顺序从0开始
                sequence = getSameLevelTaskCountExceptSelf(parentId);
            }
            task.setVersion(0);
            task.setSequence(sequence++);
            task.setStartTime(now);
            super.save(task);
        }
        return true;
    }


    @Override
    public MyPriorityQueue<Task> getTopTask() throws Exception {
        // 先检查并修复所有任务的序号
        fixAllTaskSequences();
        
        List<Task> allTasks = taskDao.selectList(null);
        // 创建一个 Map 来存储所有的任务，键是任务的 id，值是任务对象
        Map<Integer, Task> taskMap = new HashMap<>(10);
        for (Task task : allTasks) {
            taskMap.put(task.getId(), task);
        }
        MyPriorityQueue<Task> topTasks = new MyPriorityQueue<>();

        // 遍历所有的任务，将每个任务的子任务添加到它的 subTasks 列表中
        for (Task task : allTasks) {
            if (task.getParentId() != null) {
                Task parentTask = taskMap.get(task.getParentId());
                if (null == parentTask) {
                    throw new BusinessException(Code.BUSINESS_ERR, "没有父任务的子任务？数据不一致！");
                }

                if (null == parentTask.getSubTasks()) {
                    parentTask.setSubTasks(new MyPriorityQueue<Task>());
                }
                parentTask.getSubTasks().add(task);
            } else {
                topTasks.add(task);
            }
        }

        return topTasks;
    }

    /**
     * 修复所有任务的序号
     * 这个方法会检查所有层级的任务，确保序号没有重复
     */
    @Transactional
    private void fixAllTaskSequences() {
        log.info("Starting to fix all task sequences");
        
        try {
            // 先修复根任务的序号
            log.info("Fixing root task sequences");
            rebalanceSequences(null);
            
            // 获取所有非根任务
            LambdaQueryWrapper<Task> nonRootWrapper = new LambdaQueryWrapper<>();
            nonRootWrapper.isNotNull(Task::getParentId);
            List<Task> nonRootTasks = taskDao.selectList(nonRootWrapper);
            log.info("Found {} non-root tasks", nonRootTasks.size());
            
            // 获取所有父任务ID
            Set<Integer> parentIds = nonRootTasks.stream()
                .map(Task::getParentId)
                .collect(Collectors.toSet());
            log.info("Found {} unique parent IDs: {}", parentIds.size(), parentIds);
            
            // 逐个修复每个父任务下的子任务序号
            for (Integer parentId : parentIds) {
                try {
                    log.info("Starting to fix sequences for tasks under parent {}", parentId);
                    rebalanceSequences(parentId);
                    log.info("Successfully fixed sequences for tasks under parent {}", parentId);
                } catch (Exception e) {
                    log.error("Failed to fix sequences for tasks under parent {}: {}", parentId, e.getMessage());
                    throw e;
                }
            }
            
            log.info("Successfully fixed all task sequences");
        } catch (Exception e) {
            log.error("Error while fixing task sequences: {}", e.getMessage(), e);
            throw new BusinessException(Code.UPDATE_ERR, "修复任务序号失败: " + e.getMessage());
        }
    }

    /**
     * 检查指定父任务下的子任务是否有序号冲突
     */
    private boolean hasSequenceConflicts(Integer parentId) {
        List<Task> siblings = getSiblingsOrderBySequence(parentId);
        long uniqueSequences = siblings.stream()
            .map(Task::getSequence)
            .distinct()
            .count();
        return uniqueSequences != siblings.size();
    }

    @Override
    public Map<Integer, List<Task>> getTasksGroupedByParentId() {
        List<Task> allTasks = taskDao.selectList(null);
        Map<Integer, List<Task>> tasksGroupedByParentId = new HashMap<>(10);

        for (Task task : allTasks) {
            Integer parentId = task.getParentId();
            // 键为parentId对应的值不存在，就创建一个list
            tasksGroupedByParentId.putIfAbsent(parentId, new ArrayList<>());
            tasksGroupedByParentId.get(parentId).add(task);
        }
        return tasksGroupedByParentId;
    }

    @Override
    public boolean hasSubTask(Integer id) {
        return getDirectSubTaskCount(id) != 0;
    }

    private boolean hasSubTask(Integer id, Map<Integer, List<Task>> tasksGroupedByParentId) {
        return tasksGroupedByParentId.containsKey(id);
    }

    @Override
    public Set<Integer> getDirectSubTaskId(Integer id) {
        return taskDao.getDirectSubTaskId(id);
    }

    @Override
    public Integer getDirectSubTaskCount(Integer id) {
        return getDirectSubTaskId(id).size();
    }


    private Set<Integer> getDirectSubTaskId(Integer id, Map<Integer, List<Task>> tasksGroupedByParentId) {
        HashSet<Integer> ids = new HashSet<>();
        List<Task> tasks = tasksGroupedByParentId.get(id);
        for (Task task : tasks) {
            // 添加所有子任务的id
            ids.add(task.getId());
        }
        return ids;
    }

    @Override
    public Set<Integer> getBatchSubTaskId(Integer id) {
        HashSet<Integer> ids = new HashSet<>();
        Map<Integer, List<Task>> tasksGroupedByParentId = getTasksGroupedByParentId();
        getBatchSubTaskId(id, ids, tasksGroupedByParentId);
        return ids;
    }

    @Override
    public Integer getSameLevelTaskCountExceptSelf(Integer parentId) {
        return getDirectSubTaskCount(parentId);
    }

    @Override
    public boolean removeById(Integer id) {
        Task task = taskDao.selectById(id);
        // 处理空缺位
        dealWithVacancy(task.getParentId(), task.getSequence());
        return super.removeById(id);
    }

    /**
     * 重新计算同级任务的sequence，确保它们的顺序反映实际显示顺序
     * @param tasks 同级任务列表
     */
    private void recalculateSequences(List<Task> tasks) {
        if (tasks.isEmpty()) return;
        
        log.info("Recalculating sequences for {} tasks", tasks.size());
        
        // 使用较大的步长，避免频繁重新计算
        int baseStep = 10000;
        int sequence = baseStep;
        
        // 遍历列表，按顺序设置sequence
        for (Task task : tasks) {
            task.setSequence(sequence);
            taskDao.updateById(task);
            sequence += baseStep;
        }
    }

    /**
     * 在两个sequence值之间找到一个中间值
     * @param prev 前一个sequence值
     * @param next 后一个sequence值
     * @return 中间的sequence值
     */
    private int findMiddleSequence(int prev, int next) {
        // 如果两个值相等或差值小于步长，先返回较小值加上步长的一半
        if (prev == next || (next - prev) < Task.SEQUENCE_STEP) {
            return prev + (Task.SEQUENCE_STEP / 2);
        }
        // 使用long避免整数溢出
        return (int) (((long) prev + (long) next) / 2);
    }

    /**
     * 获取插入位置的sequence值
     * @param siblings 同级任务列表
     * @param insertIndex 插入位置
     * @param moveAction 移动动作（before/after/inner）
     * @return 新的sequence值
     */
    private int calculateInsertSequence(List<Task> siblings, int insertIndex, String moveAction) {
        if (siblings.isEmpty()) {
            return Task.SEQUENCE_STEP;
        }
        
        // 对siblings按sequence排序，如果sequence相同则按id排序，保证顺序稳定
        siblings.sort(Comparator.<Task>comparingInt(Task::getSequence)
                              .thenComparingInt(Task::getId));
        
        if (insertIndex == 0) {
            // 插入到最前面
            int firstSequence = siblings.get(0).getSequence();
            return firstSequence - (Task.SEQUENCE_STEP / 2);
        } else if (insertIndex >= siblings.size()) {
            // 插入到最后面
            int lastSequence = siblings.get(siblings.size() - 1).getSequence();
            return lastSequence + (Task.SEQUENCE_STEP / 2);
        } else {
            // 插入到中间
            int prevSequence = siblings.get(insertIndex - 1).getSequence();
            int nextSequence = siblings.get(insertIndex).getSequence();
            
            // 如果前后sequence相同，则在这个sequence值的基础上微调
            if (prevSequence == nextSequence) {
                return prevSequence + (moveAction.equals("before") ? -1 : 1);
            }
            
            // 计算中间值
            return (prevSequence + nextSequence) / 2;
        }
    }
    
    /**
     * 重新平衡所有任务的sequence
     */
    private void rebalanceAllSequences() {
        log.info("Starting global sequence rebalance");
        
        // 获取所有任务，按父节点分组
        Map<Integer, List<Task>> tasksByParent = new HashMap<>();
        List<Task> allTasks = taskDao.selectList(null);
        
        // 按父节点ID分组
        for (Task task : allTasks) {
            Integer parentId = task.getParentId();
            tasksByParent.computeIfAbsent(parentId, k -> new ArrayList<>()).add(task);
        }
        
        // 为每组任务重新计算sequence
        for (List<Task> tasks : tasksByParent.values()) {
            // 对每组任务按当前sequence排序
            tasks.sort(Comparator.comparing(Task::getSequence));
            
            // 使用基础步长重新计算sequence
            int stepSize = Math.max(Task.SEQUENCE_STEP / (tasks.size() + 1), 100);
            int sequence = stepSize;
            
            for (Task task : tasks) {
                task.setSequence(sequence);
                taskDao.updateById(task);
                sequence += stepSize;
            }
        }
        
        log.info("Global sequence rebalance completed");
    }

    /**
     * 计算两个序号之间的中间序号
     * @param prev 前一个序号
     * @param next 后一个序号
     * @return 中间序号
     */
    private int calculateMiddleSequence(int prev, int next) {
        // 使用long避免整数溢出
        long middle = ((long) prev + (long) next) / 2;
        return (int) middle;
    }

    /**
     * 获取列表开头的序号
     * @param firstSequence 第一个任务的序号
     * @return 新的序号
     */
    private int calculateFirstSequence(Integer firstSequence) {
        return firstSequence != null ? firstSequence - Task.SEQUENCE_STEP : Task.SEQUENCE_STEP;
    }

    /**
     * 获取列表末尾的序号
     * @param lastSequence 最后一个任务的序号
     * @return 新的序号
     */
    private int calculateLastSequence(Integer lastSequence) {
        return lastSequence != null ? lastSequence + Task.SEQUENCE_STEP : Task.SEQUENCE_STEP;
    }

    @Override
    @Transactional
    public boolean moveTask(Integer movedTaskId, Integer targetTaskId, String moveAction) {
        Task movedTask = taskDao.selectById(movedTaskId);
        if (movedTask == null) {
            throw new BusinessException(Code.BUSINESS_ERR, "任务不存在");
        }

        if (log.isDebugEnabled()) {
            log.debug("Moving task {} to target {} with action {}", movedTaskId, targetTaskId, moveAction);
        }
        
        try {
            // 移动到根级别
            if (targetTaskId == null) {
                // 记录原始父节点ID，用于后续验证
                Integer originalParentId = movedTask.getParentId();
                handleRootLevelMove(movedTask, moveAction);
                
                Task updatedTask = taskDao.selectById(movedTaskId);
                if (updatedTask == null) {
                    log.error("Task {} not found after move to root level", movedTaskId);
                    throw new BusinessException(Code.BUSINESS_ERR, "移动到根级别失败");
                }
                
                if (updatedTask.getParentId() != null) {
                    log.error("Task {} still has parent ID {} after move to root level", 
                        movedTaskId, updatedTask.getParentId());
                    throw new BusinessException(Code.BUSINESS_ERR, "移动到根级别失败，父节点未正确更新");
                }
                
                if (log.isDebugEnabled()) {
                    log.debug("Successfully moved task {} from parent {} to root level", 
                        movedTaskId, originalParentId);
                }
                
                return true;
            }
            
            Task targetTask = taskDao.selectById(targetTaskId);
            if (targetTask == null) {
                throw new BusinessException(Code.BUSINESS_ERR, "目标任务不存在");
            }

            // 检查是否形成循环引用
            if (isCircularReference(movedTask.getId(), targetTaskId)) {
                throw new BusinessException(Code.BUSINESS_ERR, "不能将任务移动到其子任务中");
            }

            switch (moveAction) {
                case "inner":
                    handleInnerMove(movedTask, targetTask);
                    break;
                case "before":
                case "after":
                    handleBeforeAfterMove(movedTask, targetTask, moveAction);
                    break;
                default:
                    throw new BusinessException(Code.BUSINESS_ERR, "不支持的移动类型");
            }
            
            Task updatedTask = taskDao.selectById(movedTaskId);
            if (updatedTask == null) {
                log.error("Task {} not found after move", movedTaskId);
                throw new BusinessException(Code.BUSINESS_ERR, "移动后任务不存在");
            }

            return true;
        } catch (Exception e) {
            log.error("Failed to move task: {}", e.getMessage());
            throw new BusinessException(Code.BUSINESS_ERR, "移动任务失败: " + e.getMessage());
        }
    }

    private void handleRootLevelMove(Task movedTask, String moveAction) {
        if (log.isDebugEnabled()) {
            log.debug("Moving task {} to root level with action {}", movedTask.getId(), moveAction);
        }
        
        List<Task> rootTasks = getSiblingsOrderBySequence(null);
        
        int newSequence;
        if (moveAction.equals("before")) {
            newSequence = rootTasks.isEmpty() ? 
                Task.SEQUENCE_STEP : 
                rootTasks.get(0).getSequence() - Task.SEQUENCE_STEP;
        } else {
            newSequence = rootTasks.isEmpty() ? 
                Task.SEQUENCE_STEP : 
                rootTasks.get(rootTasks.size() - 1).getSequence() + Task.SEQUENCE_STEP;
        }
        
        // 使用UpdateWrapper来确保原子性更新
        UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", movedTask.getId())
                    .set("parent_id", null)
                    .set("sequence", newSequence);
        
        boolean success = update(null, updateWrapper);
        
        if (!success) {
            log.error("Failed to update task {} to root level", movedTask.getId());
            throw new BusinessException(Code.BUSINESS_ERR, "移动任务到根级别失败");
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Task {} moved to root level with sequence = {}", movedTask.getId(), newSequence);
        }
    }

    /**
     * 处理移动到任务内部的情况
     */
    private void handleInnerMove(Task movedTask, Task targetTask) {
        // 获取目标任务的所有子任务
        List<Task> children = getSiblingsOrderBySequence(targetTask.getId());
        
        // 从列表中移除被移动的任务（如果存在）
        children.removeIf(task -> task.getId().equals(movedTask.getId()));
        
        // 设置新的父任务
        movedTask.setParentId(targetTask.getId());
        
        // 添加到开头
        children.add(0, movedTask);
        
        // 重新计算sequence
        reorderTasks(children);
    }

    /**
     * 处理移动到任务前后的情况
     */
    private void handleBeforeAfterMove(Task movedTask, Task targetTask, String moveAction) {
        List<Task> siblings = getSiblingsOrderBySequence(targetTask.getParentId());
        siblings.removeIf(task -> task.getId().equals(movedTask.getId()));
        
        int targetIndex = -1;
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).getId().equals(targetTask.getId())) {
                targetIndex = i;
                break;
            }
        }
        
        if (targetIndex == -1) {
            log.error("Target task {} not found in siblings list", targetTask.getId());
            throw new BusinessException(Code.BUSINESS_ERR, "目标任务位置错误");
        }
        
        int newSequence;
        if (moveAction.equals("before")) {
            if (targetIndex == 0) {
                newSequence = siblings.get(0).getSequence() - Task.SEQUENCE_STEP;
            } else {
                int prevSequence = siblings.get(targetIndex - 1).getSequence();
                int targetSequence = siblings.get(targetIndex).getSequence();
                newSequence = prevSequence + (targetSequence - prevSequence) / 2;
            }
        } else {
            if (targetIndex == siblings.size() - 1) {
                newSequence = siblings.get(targetIndex).getSequence() + Task.SEQUENCE_STEP;
            } else {
                int targetSequence = siblings.get(targetIndex).getSequence();
                int nextSequence = siblings.get(targetIndex + 1).getSequence();
                newSequence = targetSequence + (nextSequence - targetSequence) / 2;
            }
        }
        
        UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", movedTask.getId())
                    .set("sequence", newSequence)
                    .set("parent_id", targetTask.getParentId());
        
        boolean success = update(null, updateWrapper);
        if (!success) {
            log.error("Failed to move task {} relative to {}", movedTask.getId(), targetTask.getId());
            throw new BusinessException(Code.BUSINESS_ERR, "移动任务失败");
        }
    }

    /**
     * 重新排序任务列表
     * 使用固定步长为每个任务分配新的sequence
     */
    private void reorderTasks(List<Task> tasks) {
        if (tasks.isEmpty()) return;
        
        int step = 1000;
        int sequence = step;
        
        for (Task task : tasks) {
            task.setSequence(sequence);
            taskDao.updateById(task);
            sequence += step;
        }
    }

    /**
     * 检查是否形成循环引用
     */
    private boolean isCircularReference(Integer sourceId, Integer targetId) {
        if (sourceId.equals(targetId)) {
            return true;
        }
        
        Set<Integer> subTaskIds = getBatchSubTaskId(sourceId);
        return subTaskIds.contains(targetId);
    }

    /**
     * 当前任务被删除或移动到其它层级下时，本层级任务序列会有一个空缺，此方法为填补这个空缺
     * 将其后面的所有任务的序列前移一位
     *
     * @param parentId 父任务id
     * @param sequence 缺失任务序列号
     */
    private void dealWithVacancy(Integer parentId, Integer sequence) {
        // 将被删除/移动节点后的所有任务的sequence前移一位
        taskDao.moveForward(parentId, sequence);
    }


    private void getBatchSubTaskId(Integer id, Set<Integer> ids, Map<Integer, List<Task>> tasksGroupedByParentId) {
        ids.add(id);
        if (!hasSubTask(id, tasksGroupedByParentId)) {
            return;
        }
        // 深度优先搜索
        Set<Integer> subTaskId = getDirectSubTaskId(id, tasksGroupedByParentId);
        for (Integer subId : subTaskId) {
            getBatchSubTaskId(subId, ids, tasksGroupedByParentId);
        }
    }

    @Override
    public Task setTaskReminder(Integer taskId, Date reminderTime) {
        Task task = new Task();
        task.setId(taskId);
        task.setReminderTime(reminderTime);
        
        // 使用 UpdateWrapper 显式更新 reminderTime 字段，即使是 null 值
        boolean success = this.update(
            task,
            new UpdateWrapper<Task>()
                .eq("id", taskId)
                .set("reminder_time", reminderTime)
        );
    
        return success ? this.getById(taskId) : null;
    }
    
    @Override
    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void processReminders() {
        try {
            Date now = new Date();
            Date oneMinuteLater = new Date(now.getTime() + 60000); // 一分钟后
            
            // 查询即将到期的任务
            LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
            wrapper.isNotNull(Task::getReminderTime)
                   .ge(Task::getReminderTime, now)
                   .le(Task::getReminderTime, oneMinuteLater)
                   .eq(Task::getBeDeleted, false);
                   
            List<Task> tasksToRemind = taskDao.selectList(wrapper);
            
            // 处理提醒
            for (Task task : tasksToRemind) {
                log.info("Task reminder: Task {} ({}) is due soon", task.getId(), task.getDescription());
                // TODO: 实现具体的提醒方式（如WebSocket推送、邮件通知等）
            }
        } catch (Exception e) {
            log.error("Error processing reminders: ", e);
        }
    }

    @Override
    @Transactional
    public Integer updateIsDeleted(Integer id, Boolean finish) {
        log.info("Updating task {} finish state to {}", id, finish);
        
        // 获取任务
        Task task = getById(id);
        if (task == null) {
            throw new BusinessException(Code.TASK_NOT_FOUND, "任务不存在");
        }
        
        // 检查当前状态，避免重复操作
        if (task.getBeDeleted() == finish) {
            log.info("Task {} is already in desired state (finish={})", id, finish);
            return task.getSequence();
        }
        
        // 如果是标记完成，先标记所有子任务
        if (finish) {
            markSubTasksAsCompleted(id);
            // 重新获取任务状态，因为可能在标记子任务时发生了变化
            task = getById(id);
        }
        
        // 更新任务状态
        return updateTaskStateSimple(task, finish);
    }
    
    /**
     * 简化的任务状态更新方法
     */
    private Integer updateTaskStateSimple(Task task, boolean finish) {
        int retryCount = 0;
        while (retryCount < 3) {
            try {
                // 每次尝试前获取最新的任务状态
                Task freshTask = getById(task.getId());
                if (freshTask == null) {
                    throw new BusinessException(Code.TASK_NOT_FOUND, "任务不存在");
                }
                
                // 计算新的序号
                Integer newSequence;
                if (finish) {
                    // 标记为完成时，保存原始序号并设置新序号
                    newSequence = 50_000_000 + freshTask.getSequence();
                    log.info("Task {} marked as finished, saving previous sequence {} and setting new sequence {}", 
                        task.getId(), freshTask.getSequence(), newSequence);
                } else {
                    // 取消标记时，恢复到原始序号
                    newSequence = freshTask.getPreviousSequence();
                    if (newSequence == null) {
                        // 如果没有保存原始序号，使用当前序号减去偏移量
                        newSequence = freshTask.getSequence() - 50_000_000;
                    }
                    log.info("Task {} unmarked, restoring to sequence {}", task.getId(), newSequence);
                }
                
                // 创建更新条件
                UpdateWrapper<Task> updateWrapper = new UpdateWrapper<Task>()
                    .eq("id", task.getId())
                    .eq("version", freshTask.getVersion())
                    .set("is_deleted", finish)
                    .set("completion_time", finish ? new Date() : null)
                    .set("previous_sequence", finish ? freshTask.getSequence() : null)
                    .set("sequence", newSequence)
                    .set("version", freshTask.getVersion() + 1);
                
                // 使用乐观锁更新任务状态
                boolean success = update(null, updateWrapper);
                
                if (success) {
                    log.info("Successfully updated task {} state to {} with sequence {}", 
                        task.getId(), finish, newSequence);
                    return newSequence;
                }
                
                log.warn("Failed to update task {} state (version conflict), retrying...", task.getId());
                retryCount++;
                
                if (retryCount < 3) {
                    Thread.sleep(100 * retryCount);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new BusinessException(Code.UPDATE_ERR, "更新任务状态被中断");
            } catch (BusinessException be) {
                throw be;
            } catch (Exception e) {
                log.error("Error updating task {} state: {}", task.getId(), e.getMessage());
                throw new BusinessException(Code.UPDATE_ERR, "更新任务状态失败：" + e.getMessage());
            }
        }
        
        throw new BusinessException(Code.UPDATE_ERR, 
            String.format("更新任务 %d 状态失败（重试%d次）", task.getId(), retryCount));
    }
    
    /**
     * 简化的子任务标记完成方法
     */
    private void markSubTasksAsCompleted(Integer parentId) {
        // 获取所有未完成的子任务
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getParentId, parentId)
               .eq(Task::getBeDeleted, false);
        List<Task> unfinishedSubTasks = taskDao.selectList(wrapper);
        
        if (!unfinishedSubTasks.isEmpty()) {
            log.info("Marking {} subtasks of task {} as completed", unfinishedSubTasks.size(), parentId);
            
            // 标记每个子任务为完成
            for (Task subTask : unfinishedSubTasks) {
                try {
                    updateTaskStateSimple(subTask, true);
                    log.debug("Marked subtask {} as completed", subTask.getId());
                } catch (Exception e) {
                    log.error("Failed to mark subtask {} as completed: {}", subTask.getId(), e.getMessage());
                    throw new BusinessException(Code.UPDATE_ERR, "标记子任务完成状态失败");
                }
            }
        }
    }

    @Override
    public Map<String, Object> getTaskDetails(Integer taskId) {
        return taskDao.getTaskDetails(taskId);
    }

    @Override
    public List<Map<String, Object>> getSiblingSequences(Integer parentId) {
        return taskDao.getSiblingSequences(parentId);
    }

    /**
     * 定时任务：每天凌晨3点执行序号重平衡
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void scheduleSequenceRebalancing() {
        if (log.isDebugEnabled()) {
            log.debug("Starting scheduled sequence rebalancing");
        }
        
        try {
            List<Task> allTasks = taskDao.selectList(null);
            Map<Integer, List<Task>> tasksByParent = allTasks.stream()
                .collect(Collectors.groupingBy(t -> t.getParentId() == null ? -1 : t.getParentId()));
            
            List<Task> rootTasks = tasksByParent.getOrDefault(-1, new ArrayList<>());
            if (!rootTasks.isEmpty()) {
                rebalanceTaskGroup(null, rootTasks);
            }
            
            tasksByParent.forEach((parentId, tasks) -> {
                if (parentId != -1) {
                    rebalanceTaskGroup(parentId, tasks);
                }
            });
            
            if (log.isDebugEnabled()) {
                log.debug("Scheduled sequence rebalancing completed successfully");
            }
        } catch (Exception e) {
            log.error("Error during scheduled sequence rebalancing: {}", e.getMessage());
        }
    }
    
    /**
     * 重平衡一组任务的序号
     */
    private void rebalanceTaskGroup(Integer parentId, List<Task> tasks) {
        if (tasks.isEmpty()) return;
        
        try {
            List<Task> unfinishedTasks = new ArrayList<>();
            List<Task> finishedTasks = new ArrayList<>();
            
            for (Task task : tasks) {
                if (task.getBeDeleted()) {
                    finishedTasks.add(task);
                } else {
                    unfinishedTasks.add(task);
                }
            }
            
            unfinishedTasks.sort(Comparator.comparing(Task::getSequence));
            finishedTasks.sort(Comparator.comparing(Task::getSequence));
            
            int baseSequence = parentId == null ? 1_000_000 : 100_000;
            int step = 100_000;
            
            int currentSequence = baseSequence;
            for (Task task : unfinishedTasks) {
                updateTaskSequenceIfNeeded(task, currentSequence);
                currentSequence += step;
            }
            
            currentSequence = baseSequence + 50_000_000;
            for (Task task : finishedTasks) {
                updateTaskSequenceIfNeeded(task, currentSequence);
                currentSequence += step;
            }
        } catch (Exception e) {
            log.error("Error rebalancing tasks under parent {}: {}", parentId, e.getMessage());
        }
    }
    
    /**
     * 仅在序号不同时更新任务序号，带重试机制
     */
    private void updateTaskSequenceIfNeeded(Task task, int newSequence) {
        if (task.getSequence() == newSequence) {
            return;
        }
        
        int retryCount = 0;
        Task freshTask = null;
        
        while (retryCount < 3) {
            try {
                freshTask = getById(task.getId());
                if (freshTask == null) {
                    log.error("Task {} not found during sequence update", task.getId());
                    return;
                }
                
                if (freshTask.getSequence() == newSequence) {
                    return;
                }
                
                UpdateWrapper<Task> updateWrapper = new UpdateWrapper<Task>()
                    .eq("id", task.getId())
                    .eq("version", freshTask.getVersion())
                    .set("sequence", newSequence)
                    .set("version", freshTask.getVersion() + 1);
                
                boolean success = update(null, updateWrapper);
                if (success) {
                    task.setVersion(freshTask.getVersion() + 1);
                    task.setSequence(newSequence);
                    return;
                }
                
                retryCount++;
                if (retryCount < 3) {
                    Thread.sleep(100 * retryCount);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount < 3) {
                    try {
                        Thread.sleep(100 * retryCount);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
        
        log.error("Failed to update sequence for task {} after {} retries", task.getId(), retryCount);
        throw new BusinessException(Code.UPDATE_ERR, 
            String.format("更新任务 %d 序号失败（重试%d次），请重试", task.getId(), retryCount));
    }
}

