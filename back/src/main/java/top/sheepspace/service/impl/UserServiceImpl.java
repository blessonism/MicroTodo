package top.sheepspace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sheepspace.mapper.UserMapper;
import top.sheepspace.pojo.User;
import top.sheepspace.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Override
    public User findByGithubId(String githubId) {
        return this.lambdaQuery()
                .eq(User::getGithubId, githubId)
                .one();
    }
    
    @Override
    public User createOrUpdateGithubUser(String githubId, String login, String email, String avatarUrl, String name) {
        // 先查找是否存在该 GitHub 用户
        User user = findByGithubId(githubId);
        
        if (user == null) {
            logger.info("No existing user found with GitHub ID: {}", githubId);
            
            // 检查邮箱是否已被使用
            if (email != null && !email.isEmpty()) {
                logger.info("Checking if email {} is already registered", email);
                user = this.lambdaQuery()
                        .eq(User::getEmail, email)
                        .one();
                
                if (user != null) {
                    logger.info("Found existing user with matching email. Linking GitHub account to user: {}", user.getUname());
                    // 如果找到匹配邮箱的用户，更新 GitHub 信息
                    user.setGithubId(githubId);
                    // user.setAvatar(avatarUrl);
                    if (name != null && !name.isEmpty()) {
                        user.setDisplayName(name);
                    }
                    this.updateById(user);
                    logger.info("Successfully linked GitHub account to existing user");
                    return user;
                }
            }
            
            // 创建新用户
            logger.info("Creating new user for GitHub account");
            user = new User();
            user.setGithubId(githubId);
            
            // 使用 GitHub 登录名作为用户名，如果冲突则添加随机后缀
            String username = login;
            int attempts = 0;
            while (this.lambdaQuery().eq(User::getUname, username).one() != null) {
                attempts++;
                username = login + "_" + UUID.randomUUID().toString().substring(0, 6);
                logger.info("Username {} already exists, trying alternative: {} (attempt {})", login, username, attempts);
                if (attempts > 5) {
                    logger.error("Failed to generate unique username after {} attempts", attempts);
                    throw new RuntimeException("无法生成唯一用户名，请稍后重试");
                }
            }
            
            user.setUname(username);
            user.setDisplayName(name != null && !name.isEmpty() ? name : login);
            user.setEmail(email);
            user.setAvatar(avatarUrl);
            user.setPassword(UUID.randomUUID().toString()); // 设置随机密码
            
            try {
                this.save(user);
                logger.info("Successfully created new user: {}", username);
            } catch (Exception e) {
                logger.error("Failed to create new user", e);
                throw new RuntimeException("创建用户失败：" + e.getMessage());
            }
        } else {
            logger.info("Updating existing user: {}", user.getUname());
            // 更新现有用户信息
            boolean updated = false;
            if (email != null && !email.equals(user.getEmail())) {
                user.setEmail(email);
                updated = true;
            }
            if (avatarUrl != null && !avatarUrl.equals(user.getAvatar())) {
                user.setAvatar(avatarUrl);
                updated = true;
            }
            if (name != null && !name.isEmpty() && !name.equals(user.getDisplayName())) {
                user.setDisplayName(name);
                updated = true;
            }
            
            if (updated) {
                try {
                    this.updateById(user);
                    logger.info("Successfully updated user information");
                } catch (Exception e) {
                    logger.error("Failed to update user information", e);
                    throw new RuntimeException("更新用户信息失败：" + e.getMessage());
                }
            } else {
                logger.info("No changes detected, skipping update");
            }
        }
        
        return user;
    }
}
