package top.sheepspace.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author Sheep
 * @since 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_task")
public class Task implements Serializable, Comparable<Task> {

    private static final long serialVersionUID = 1L;

    /**
     * sequence的默认步长，用于在任务之间留出空间，避免频繁更新
     */
    public static final int SEQUENCE_STEP = 1000;

    /**
     * sequence的最大值，用于在需要时重新平衡sequence
     */
    public static final int MAX_SEQUENCE = Integer.MAX_VALUE - SEQUENCE_STEP;

    /**
     * 任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private String endTime;

    /**
     * 父任务ID
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 任务类型
     */
    private String type;

    /**
     * 任务状态
     */
    private String state;

    /**
     * 任务优先级，越小表示优先级越高
     */
    private Integer priority;

    /**
     * 任务顺序，越小表示越靠前
     */
    private Integer sequence;

    /**
     * 是否已完成
     */
    @TableField(value = "is_deleted")
    private Boolean beDeleted;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

    /**
     * 子任务列表，非数据库字段，需要使用 @TableField(exist = false) 标注
     */
    @TableField(exist = false)
    private MyPriorityQueue<Task> subTasks;

    /**
     * 提醒时间
     */
    @TableField("reminder_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reminderTime;

    /**
     * 任务完成时间
     */
    @TableField("completion_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completionTime;

    /**
     * 任务完成前的序号（用于短时间内取消完成时恢复位置）
     */
    @TableField("previous_sequence")
    private Integer previousSequence;

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }

    @Override
    public int compareTo(Task other) {
        // 1. 首先按照完成状态排序，未完成的任务在前面
        int completedCompare = this.beDeleted.compareTo(other.beDeleted);
        if (completedCompare != 0) {
            return completedCompare;
        }

        // 2. 在相同完成状态下，按照sequence排序（越小越靠前）
        return this.sequence.compareTo(other.sequence);
    }
}
