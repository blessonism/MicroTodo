package top.sheepspace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepspace.pojo.Task;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.service
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-04-23 - 9:30
 */
@SpringBootTest
public class ITaskServiceTest {
    @Autowired
    ITaskService taskService;

    @Test
    public void testType() throws Exception {
        String name = taskService.getTopTask().poll().getSubTasks().getClass().getName();
        // name = java.util.PriorityQueue
        System.out.println("name = " + name);
    }

    @Test
    public void getTopTask() throws Exception {
        PriorityQueue<Task> topTask = taskService.getTopTask();
        PriorityQueue<Task> subTasks = topTask.poll().getSubTasks();
        System.out.println(subTasks);
        PriorityQueue<Task> temp = new PriorityQueue<>();
        Task t;
        while (!subTasks.isEmpty()){
            t = subTasks.poll();
            System.out.println("#####");
            System.out.println(t);
            System.out.println("#####");

            temp.add(t);
        }
        System.out.println(temp);

        assertNotNull(topTask);
    }

    @Test
    public void getTasksGroupedByParentId() {
        Map<Integer, List<Task>> grouped = taskService.getTasksGroupedByParentId();
        System.out.println(grouped);
        assertNotNull(grouped);
    }

    @Test
    public void hasSubTask() {
        assertTrue(taskService.hasSubTask(1));
        assertTrue(taskService.hasSubTask(4));
        assertFalse(taskService.hasSubTask(13));

    }

    @Test
    public void getSubTaskId() {
        Set<Integer> subTaskId = taskService.getBatchSubTaskId(1);
        System.out.println(subTaskId);
        assertNotNull(subTaskId);
    }

    @Test
    public void getSubTaskId2() {
        Set<Integer> subTaskId = taskService.getBatchSubTaskId(13);
        System.out.println(subTaskId);
        assertNotNull(subTaskId);
    }

    @Test
    public void getSubTaskId3() {
        Set<Integer> subTaskId = taskService.getBatchSubTaskId(3);
        System.out.println(subTaskId);
        assertNotNull(subTaskId);
    }

    @Test
    public void getSubTaskId4() {
        Set<Integer> subTaskId = taskService.getBatchSubTaskId(9);
        System.out.println(subTaskId);
        assertNotNull(subTaskId);
    }

    @Test
    public void testMoveInner() {
        Integer movedTaskId = 11;
        Integer targetTaskId = 23;
        taskService.moveInner(movedTaskId, targetTaskId);
    }

    @Test
    public void testMoveBefore() {
        Integer movedTaskId = 11;
        Integer targetTaskId = 23;
        taskService.moveToBefore(movedTaskId, targetTaskId);
    }

    @Test
    public void testMoveAfter() {
        Integer movedTaskId = 11;
        Integer targetTaskId = 23;
        taskService.moveToAfter(movedTaskId, targetTaskId);
    }


}