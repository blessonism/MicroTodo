package top.sheepspace.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.pojo
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-05-08 - 18:55
 */
@JsonSerialize(using = MyPriorityQueueSerializer.class)
public class MyPriorityQueue<T> extends PriorityQueue<T> {
}
