package top.sheepspace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sheepspace.pojo.User;

public interface IUserService extends IService<User> {
    /**
     * 根据 GitHub ID 查找用户
     * @param githubId GitHub ID
     * @return 用户对象，如果不存在则返回 null
     */
    User findByGithubId(String githubId);
    
    /**
     * 创建或更新 GitHub 用户
     * @param githubId GitHub ID
     * @param login GitHub 登录名
     * @param email 邮箱
     * @param avatarUrl 头像 URL
     * @param name 显示名称
     * @return 用户对象
     */
    User createOrUpdateGithubUser(String githubId, String login, String email, String avatarUrl, String name);
} 