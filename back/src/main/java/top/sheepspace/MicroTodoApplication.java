package top.sheepspace;

import io.github.asleepyfish.annotation.EnableChatGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 28297
 */
@SpringBootApplication
@EnableChatGPT
public class MicroTodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroTodoApplication.class, args);
    }

}
