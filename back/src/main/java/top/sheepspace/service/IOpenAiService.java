package top.sheepspace.service;

import top.sheepspace.pojo.Task;

import java.util.List;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.service
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-05-06 - 16:33
 */
public interface IOpenAiService {


    /**
     * 与gpt对话获取回答
     * @param prompt 提示词
     * @return 对话返回结果
     */
    public List<String> getAnswer(String prompt);

    /**
     * 通过当前任务描述生成子任务描述
     * @param taskDescription 当前任务描述
     * @return 子任务描述，形式为一个json字符串
     */
    public List<String> getSubTasksDesc(String taskDescription);

}
