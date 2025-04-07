package top.sheepspace.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sheepspace.pojo.User;

@Mapper
public interface UserDao extends BaseMapper<User> {
}