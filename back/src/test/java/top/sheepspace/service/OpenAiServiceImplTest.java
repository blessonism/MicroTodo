package top.sheepspace.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepspace.pojo.MyPriorityQueue;
import top.sheepspace.pojo.Task;
import top.sheepspace.service.impl.OpenAiServiceImpl;

import java.util.*;

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
public class OpenAiServiceImplTest {
    @Autowired
    OpenAiServiceImpl openAiService;

    @Test
    public void testGetSubTasks() throws Exception {
        String taskDesc = "做蛋炒饭";
        List<String> subTasksDesc = openAiService.getSubTasksDesc(taskDesc);
        System.out.println(subTasksDesc);
    }

    @Test
    public void testCompareBool() {
        Boolean b1 = true;
        Boolean b2 = false;
        System.out.println(b1.compareTo(b2));
    }


    @Test
    public void testCompareTask() {
        Task task1 = new Task();
        task1.setBeDeleted(true);
        task1.setSequence(3);
        task1.setDescription("task1");
        Task task2 = new Task();
        task2.setBeDeleted(false);
        task2.setSequence(5);
        task2.setDescription("task2");

        System.out.println(task1.compareTo(task2));

        Task parentTask = new Task();
        parentTask.setSubTasks(new MyPriorityQueue<>());
        parentTask.getSubTasks().add(task1);
        parentTask.getSubTasks().add(task2);
        System.out.println(parentTask.getSubTasks());

    }
}