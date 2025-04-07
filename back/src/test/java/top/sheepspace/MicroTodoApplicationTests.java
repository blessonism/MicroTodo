package top.sheepspace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.sheepspace.dao.TaskDao;
import top.sheepspace.pojo.Task;
import top.sheepspace.service.ITaskService;

import java.util.*;

@SpringBootTest
class MicroTodoApplicationTests {
    @Autowired
    TaskDao taskDao;

    @Autowired
    ITaskService taskService;

    @Test
    public void testPath(){
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void testSelectAll(){
        List<Task> tasks = taskDao.selectList(null);
        System.out.println(tasks);
    }



    @Test
    public void testPassValue(){
        HashMap<String, Node> map = new HashMap<>();
        Node node1_1_1 = new Node("任务1-1-1", null);
        Node node1_1 = new Node("任务1-1", null);
        Node node1 = new Node("任务1-1", null);

        node1.setSubNode(node1_1);
        // Node{value='任务1-1', subNode=Node{value='任务1-1', subNode=null}}
        System.out.println(node1);
        node1_1.setSubNode(node1_1_1);
        System.out.println("----");
        // Node{value='任务1-1', subNode=Node{value='任务1-1', subNode=Node{value='任务1-1-1', subNode=null}}}
        System.out.println(node1);
    }

    @Test
    public void testGetTopTask() throws Exception {
        PriorityQueue<Task> topTask =  taskService.getTopTask();
        System.out.println(topTask);
    }

    @Test
    public void testTaskCount(){
        int count = taskService.count();
        System.out.println("count = " + count);
    }

    @Test
    public void testGroupByParentId(){
        Map<Integer, List<Task>> grouped = taskService.getTasksGroupedByParentId();
        System.out.println(grouped);
    }
    
    @Test
    public void testEmptyIter(){
        ArrayList<Integer> list = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        for (Integer num : list) {
            set.add(num);
        }
        System.out.println(set);
    }


    @Test
    void contextLoads() {
    }

}

class Node{
    private String value;
    private Node subNode;

    public Node() {
    }

    public Node(String value, Node subNode) {
        this.value = value;
        this.subNode = subNode;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", subNode=" + subNode +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getSubNode() {
        return subNode;
    }

    public void setSubNode(Node subNode) {
        this.subNode = subNode;
    }
}
