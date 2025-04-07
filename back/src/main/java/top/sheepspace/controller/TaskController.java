package top.sheepspace.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.sheepspace.pojo.MoveRequestBody;
import top.sheepspace.pojo.MyPriorityQueue;
import top.sheepspace.pojo.ReminderRequest;
import top.sheepspace.pojo.Task;
import top.sheepspace.service.impl.TaskServiceImpl;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Sheep
 * @since 2024-04-22
 */
@RestController
//@CrossOrigin//可以加在类上，也可以加到方法上
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskServiceImpl taskService;

    @PostMapping
    public Result saveTask(@RequestBody Task task) {
        boolean success = taskService.save(task);
        return new Result(success ? Code.SAVE_OK : Code.SAVE_ERR, success);
    }

    @PostMapping("/split/{id}")
    public Result splitTask(@PathVariable Integer id) {
        boolean success = taskService.splitSubTasks(id);
        return new Result(success ? Code.SAVE_OK : Code.SAVE_ERR, success);
    }

    @PostMapping("/batch")
    public Result saveTask(@RequestBody ArrayList<Task> task) {
        boolean success = taskService.saveBatch(task);
        return new Result(success ? Code.SAVE_OK : Code.SAVE_ERR, success);
    }

    @DeleteMapping("/{id}")
    public Result deleteTask(@PathVariable Integer id) {
        boolean success = taskService.removeById(id);
        return new Result(success ? Code.DELETE_OK : Code.DELETE_ERR, success);
    }


    @PutMapping
    public Result update(@RequestBody Task task) {
        boolean flag = taskService.updateById(task);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag);
    }

    @PutMapping("/move")
    public Result moveTask(@RequestBody MoveRequestBody moveRequestBody) {
        boolean success = taskService.moveTask(
            moveRequestBody.getMovedTaskId(),
            moveRequestBody.getTargetTaskId(),
            moveRequestBody.getMoveAction()
        );
        
        return success ? new Result(Code.SAVE_OK, null, "移动成功") :
                        new Result(Code.SAVE_ERR, null, "移动失败");
    }

    @PutMapping("/{id}/finish")
    public Result changeFinish(@PathVariable Integer id, @RequestParam Boolean finish) {
        Integer newSequence = taskService.updateIsDeleted(id, finish);
        return new Result(Code.UPDATE_OK, newSequence, "更新成功");
    }


    @GetMapping("/{id}")
    public Result getTaskById(@PathVariable Integer id) {
        Task data = taskService.getById(id);
        Integer code = data != null ? Code.GET_OK : Code.GET_ERR;
        String msg = data != null ? "" : "数据查询失败，请重试！";
        return new Result(code, data, msg);
    }

    @GetMapping("/{id}/subTasksId")
    public Result getSubTasksById(@PathVariable Integer id) {
        Set<Integer> data = taskService.getBatchSubTaskId(id);
        Integer code = data != null ? Code.GET_OK : Code.GET_ERR;
        String msg = data != null ? "" : "数据查询失败，请重试！";
        return new Result(code, data, msg);
    }


    @GetMapping
    public Result getTopTask() throws Exception {
        System.out.println("Top Task");
        MyPriorityQueue<Task> topTask = taskService.getTopTask();
        Integer code = topTask != null ? Code.GET_OK : Code.GET_ERR;
        String msg = topTask != null ? "" : "数据查询失败，请重试！";
        return new Result(code, topTask, msg);
    }

    @PutMapping("/{id}/reminder")
    @CrossOrigin
    public Result setTaskReminder(
            @PathVariable("id") Integer id,
            @RequestBody(required = true) ReminderRequest request) {
        try {
            // ... existing code ...
            if (request == null) {
                return new Result(Code.SET_REMINDER_ERR, null, "请求不能为空");
            }
            // 允许 reminderTime 为 null
            Task updatedTask = taskService.setTaskReminder(id, request.getReminderTime());
            return new Result(Code.SET_REMINDER_OK, updatedTask, "设置提醒成功");
        } catch (Exception e) {
            return new Result(Code.SET_REMINDER_ERR, null, "设置提醒失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}/details")
    public Result getTaskDetails(@PathVariable Integer id) {
        Map<String, Object> taskDetails = taskService.getTaskDetails(id);
        if (taskDetails == null) {
            return new Result(Code.GET_ERR, null, "任务不存在");
        }
        return new Result(Code.GET_OK, taskDetails);
    }

    @GetMapping("/siblings/{parentId}")
    public Result getSiblingSequences(@PathVariable Integer parentId) {
        List<Map<String, Object>> sequences = taskService.getSiblingSequences(parentId);
        return new Result(Code.GET_OK, sequences);
    }
}

