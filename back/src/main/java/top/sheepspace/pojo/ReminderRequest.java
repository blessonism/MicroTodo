package top.sheepspace.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ReminderRequest {
    // @NotNull(message = "提醒时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reminderTime;
    
    public ReminderRequest() {
    }
    
    public ReminderRequest(Date reminderTime) {
        this.reminderTime = reminderTime;
    }
} 