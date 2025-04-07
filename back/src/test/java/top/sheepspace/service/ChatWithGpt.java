package top.sheepspace.service;

import io.github.asleepyfish.annotation.EnableChatGPT;
import io.github.asleepyfish.util.OpenAiUtils;
import top.sheepspace.service.impl.OpenAiServiceImpl;

import java.util.List;
import java.util.Scanner;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.service
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-05-07 - 9:43
 */
@EnableChatGPT
public class ChatWithGpt {
    public static void main(String[] args) {
        OpenAiServiceImpl openAiService = new OpenAiServiceImpl();
        Scanner sc = new Scanner(System.in);

        String ques = "";
        List<String> answer;
        while (true){
            System.out.print("Q: ");
            ques = sc.nextLine();
            if("q".equalsIgnoreCase(ques)){
                break;
            }
            OpenAiUtils.createStreamChatCompletion(ques);
            System.out.println();
        }
    }
}
