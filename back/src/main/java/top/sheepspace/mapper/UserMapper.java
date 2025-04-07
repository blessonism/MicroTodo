package top.sheepspace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sheepspace.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus 会自动实现基础的 CRUD 方法
} 