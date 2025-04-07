package top.sheepspace.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.pojo
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-05-08 - 18:54
 */
public class  MyPriorityQueueSerializer extends JsonSerializer<MyPriorityQueue<?>> {
    @Override
    public void serialize(MyPriorityQueue<?> queue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Object> sortedList = queue.stream().sorted().collect(Collectors.toList());
        jsonGenerator.writeObject(sortedList);
    }
}
