package top.sheepspace.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String uname;
    private String password;
    private String displayName;
    private String avatar;
    
    // GitHub相关字段
    private String githubId;
    private String email;
    private String accessToken;
} 