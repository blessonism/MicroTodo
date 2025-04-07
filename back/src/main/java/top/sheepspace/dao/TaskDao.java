package top.sheepspace.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.sheepspace.pojo.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sheepspace.pojo.Task;

import java.util.Set;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Sheep
 * @since 2024-04-22
 */
@Mapper
public interface TaskDao extends BaseMapper<Task> {

    /**
     * 获取当前任务的所有子任务id
     *
     * @param id 当前任务id
     * @return 所有子任务id
     */
    @Select("SELECT id FROM tb_task WHERE (parent_id = #{id} OR #{id} IS NULL)")
    Set<Integer> getDirectSubTaskId(@Param("id") Integer id);

    /**
     * 将大于sequence的任务的sequence前移，目的是为了填补前面少了一个任务的空缺
     *
     * @param parentId 父任务id
     * @param sequence 任务序列值（被移走/删除任务原序列）
     */
    @Update("UPDATE tb_task SET sequence = sequence - 1 " +
            "WHERE parent_id = #{parentId} " +
            "AND sequence > #{sequence}")
    void moveForward(@Param("parentId") Integer parentId,
                     @Param("sequence") Integer sequence);

    /**
     * 将parentId任务序列为sequence及其以后的任务后移一位
     * 原因是在sequence处插入了一个新任务，所以其后面的需要后移
     *
     * @param parentId 父任务id
     * @param sequence 任务序列值（被移走/删除任务原序列）
     */
    @Update("UPDATE tb_task SET sequence = sequence + 1 " +
            "WHERE parent_id = #{parentId} " +
            "AND sequence >= #{sequence}")
    void setBack(@Param("parentId") Integer parentId,
                 @Param("sequence") Integer sequence);

    /**
     * 这里处理的是中间的其它任务，包括目标任务
     * 处理同层任务，前面的任务后移。故此间的任务当都要前移-1
     *
     * @param parentId         父任务id
     * @param originalSequence 被移动任务原来的序列号
     * @param newSequence      新序列号
     */
    @Update("UPDATE tb_task SET sequence = sequence - 1 " +
            "WHERE (parent_id = #{parentId} OR #{parentId} IS NULL) " +
            "AND sequence > #{originalSequence} AND sequence <= #{newSequence}")
    void moveForwardBetween(@Param("parentId") Integer parentId,
                            @Param("originalSequence") Integer originalSequence,
                            @Param("newSequence") Integer newSequence);

    /**
     * 同parent_id下,
     * 改为大于originalSequence小于等于newSequence的记录的sequence+1
     * 处理同层任务，后面的任务前移。故此间的任务当都要后移+1
     *
     * @param parentId         父任务i
     * @param originalSequence 被移动任务原来的序列号
     * @param newSequence      新序列号
     */
    @Update("UPDATE tb_task SET sequence = sequence + 1 " +
            "WHERE (parent_id = #{parentId} OR #{parentId} IS NULL) " +
            "AND sequence >= #{newSequence} AND sequence < #{originalSequence}")
    void setBackBetween(@Param("parentId") Integer parentId,
                        @Param("originalSequence") Integer originalSequence,
                        @Param("newSequence") Integer newSequence);

    /**
     * 获取任务的详细信息，包括sequence值
     * @param taskId 任务ID
     * @return 任务详细信息
     */
    @Select("SELECT id, description, parent_id, sequence, is_deleted FROM tb_task WHERE id = #{taskId}")
    Map<String, Object> getTaskDetails(Integer taskId);

    /**
     * 获取同级任务的sequence值
     * @param parentId 父任务ID
     * @return 同级任务列表
     */
    @Select("SELECT id, description, sequence FROM tb_task WHERE parent_id = #{parentId} ORDER BY sequence")
    List<Map<String, Object>> getSiblingSequences(Integer parentId);

}
