package top.sheepspace.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.asleepyfish.util.OpenAiUtils;
import org.springframework.stereotype.Service;
import top.sheepspace.controller.Code;
import top.sheepspace.exception.BusinessException;
import top.sheepspace.service.IOpenAiService;

import java.util.List;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.service.impl
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-05-06 - 16:47
 */
@Service
public class OpenAiServiceImpl implements IOpenAiService {

    @Override
    public List<String> getSubTasksDesc(String taskDescription) {
        String splitTaskPrompt = getSplitTaskPrompt(taskDescription);
        String answer = getAnswer(splitTaskPrompt).get(0);
        answer = preprocess(answer);
        List<String> tasks;
        ObjectMapper mapper = new ObjectMapper();
        try {
            tasks = mapper.readValue(answer, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            answer = getAnswer("The format is not correct, please reply again!" +
                    "json string information should not be included in the code block." +
                    "For example: '['subtask1', 'subtask2']'").get(0);
            answer = preprocess(answer);
            try {
                tasks = mapper.readValue(answer, new TypeReference<List<String>>() {
                });
            } catch (Exception ex) {
                // 转两次不正确，直接怕抛出异常
                e.printStackTrace();
                throw new BusinessException(Code.BUSINESS_ERR, "gpt回答格式不正确，转换异常");
            }

        }

        return tasks;
    }

    /**
     * 调用指定模型进行对话，获得回答
     *
     * @param prompt 提示词
     * @return 回答
     */
    @Override
    public List<String> getAnswer(String prompt) {
        return OpenAiUtils.createChatCompletion(prompt, "testUser");
    }


    /**
     * @param taskDescription 基本任务
     * @return 划分任务提示词
     */
    private String getSplitTaskPrompt(String taskDescription) {
        // 转义引号以避免JSON解析错误
        String escapedTaskDescription =
                taskDescription.replace("\"", "\\\"");


        String langPrompt = "Please answer me in what language this text is: '" + escapedTaskDescription + "'." +
                "Just answer one word about the language. Such as Chinese/English";

        List<String> lang;
        try {
            lang = OpenAiUtils.createChatCompletion(langPrompt, "langUser");
        } catch (Exception e) {
            return "Failed to parse language detection JSON: " + e.getMessage();
        }

        // Assume lang is a list and take the first element as the detected language
        String basePrompt = "Please, as a todoListGPT, " +
                "split the task: '" + escapedTaskDescription + "' into the appropriate todolist " +
                "and return it to me as a json array in the order it needs to be executed\n" +
                "Note: Only the json string is required, " +
                "json string information should not be included in the code block." +
                "do not add additional content, " +
                "and ensure that the message you return " +
                "can be successfully parsed into an object by the json module." +
                "For example: '['subtask1', 'subtask2']'" +
                "**You must answer in " + lang.get(0) + "**";

        return basePrompt;
    }


    private String preprocess(String content) {
        // 去除字符串首尾的空白字符
        content = content.trim();

        // 如果字符串首或尾有```, 则去除
        if (content.startsWith("```")) {
            content = content.substring(3);
        }
        if (content.endsWith("```")) {
            content = content.substring(0, content.length() - 3);
        }

        return content;
    }
}
