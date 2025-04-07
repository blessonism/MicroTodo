package top.sheepspace.pojo;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.pojo
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-04-26 - 8:13
 */
@Data
public class MoveRequestBody {
    private Integer movedTaskId;
    private Integer targetTaskId;
    private String moveAction;
}
