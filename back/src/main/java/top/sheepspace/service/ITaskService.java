package top.sheepspace.service;

import org.springframework.transaction.annotation.Transactional;
import top.sheepspace.pojo.MyPriorityQueue;
import top.sheepspace.pojo.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sheep
 * @since 2024-04-22
 */
@Transactional(rollbackFor = Exception.class)
public interface ITaskService extends IService<Task> {

    /**
     * 将当前任务划分衍生出子任务
     * @param parentId 父任务id
     * @return 是否添加成功
     */
    public boolean splitSubTasks(Integer parentId);

    /**
     * 完成任务装配，获得顶层任务列表
     *
     * @return 顶层任务列表
     * @throws Exception 业务代码异常
     */
    public MyPriorityQueue<Task> getTopTask() throws Exception;

    /**
     * 获取任务按父任务ID分组
     *
     * @return 键为外键，值为分组
     */
    public Map<Integer, List<Task>> getTasksGroupedByParentId();

    /**
     * 查询该任务是否有子任务
     *
     * @param id 主键ID
     * @return 是否有子任务
     */
    public boolean hasSubTask(Integer id);

    /**
     * 获取直接子任务id（不包括本任务id）
     *
     * @param id 该任务id
     * @return 直接子任务id
     */
    public Set<Integer> getDirectSubTaskId(Integer id);

    /**
     * 获取直接子任务的个数
     *
     * @param id 该任务id
     * @return 直接子任务个数
     */
    public Integer getDirectSubTaskCount(Integer id);

    /**
     * 获取该任务及其所有子任务的id
     *
     * @param id 该任务的id
     * @return 返回一个包含该任务及其所有子任务id的集合
     */
    public Set<Integer> getBatchSubTaskId(Integer id);

    /**
     * 获取同层任务数（除了自身，或者说当前任务还没添加，看看要添加层现存在任务数）
     * @param parentId 当前任务的父任务id
     * @return 同层任务数
     */
    public Integer getSameLevelTaskCountExceptSelf(Integer parentId);

    /**
     * 删除节点
     * @param id 节点id
     * @return 是否删除成功
     */
    public  boolean removeById(Integer id);

    /**
     * 移动任务
     * @param movedTaskId 被移动的任务ID
     * @param targetTaskId 目标任务ID，如果是移动到根级别则为null
     * @param moveAction 移动类型：before, after, inner
     * @return 是否移动成功
     */
    boolean moveTask(Integer movedTaskId, Integer targetTaskId, String moveAction);

    /**
     * 修改复选框
     * @param id 任务id
     * @param finish 修改状态
     * @return 影响行数，0则修改失败
     */
    public Integer updateIsDeleted(Integer id, Boolean finish);

    /**
     * 设置任务提醒时间
     * @param taskId 任务ID
     * @param reminderTime 提醒时间
     * @return 更新后的任务
     */
    Task setTaskReminder(Integer taskId, Date reminderTime);
    
    /**
     * 检查并处理到期提醒
     */
    void processReminders();

    /**
     * 获取任务的详细信息
     * @param taskId 任务ID
     * @return 任务详细信息
     */
    Map<String, Object> getTaskDetails(Integer taskId);

    /**
     * 获取同级任务的sequence值
     * @param parentId 父任务ID
     * @return 同级任务列表
     */
    List<Map<String, Object>> getSiblingSequences(Integer parentId);

}