package top.sheepspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sheepspace.pojo.User;
import top.sheepspace.service.IUserService;
import top.sheepspace.utils.FileUploadUtil;
import top.sheepspace.controller.Code;
import top.sheepspace.controller.Result;

import java.io.IOException;
import java.util.List;

/**
 * @Author:Zikun Zhang
 * @Student ID(NCHU):21224131
 * @Student ID(NCI):X21205833
 * @Date: 2024/6/3 下午7:32
 * @Description:
 **/

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 根据用户名获取用户信息
     *
     * @param uname 用户名
     * @return 用户信息
     */
    @GetMapping("/uname/{uname}")
    public User getUserByUname(@PathVariable String uname) {
        return userService.lambdaQuery().eq(User::getUname, uname).one();
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    /**
     * 获取所有用户信息
     *
     * @return 所有用户信息
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.list();
    }

    /**
     * 更新用户显示名称
     * @param userId 用户ID
     * @param displayName 新的显示名称
     * @return 更新结果
     */
    @PutMapping("/{userId}/displayName")
    public Result updateDisplayName(@PathVariable Integer userId, @RequestParam String displayName) {
        User user = userService.getById(userId);
        if (user == null) {
            return new Result(Code.GET_ERR, null, "用户不存在");
        }
        
        user.setDisplayName(displayName);
        boolean success = userService.updateById(user);
        return new Result(success ? Code.UPDATE_OK : Code.UPDATE_ERR, success, 
            success ? "更新显示名称成功" : "更新显示名称失败");
    }

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param avatar 新的头像URL
     * @return 更新结果
     */
    @PutMapping("/{userId}/avatar")
    public Result updateAvatar(@PathVariable Integer userId, @RequestParam String avatar) {
        User user = userService.getById(userId);
        if (user == null) {
            return new Result(Code.GET_ERR, null, "用户不存在");
        }
        
        user.setAvatar(avatar);
        boolean success = userService.updateById(user);
        return new Result(success ? Code.UPDATE_OK : Code.UPDATE_ERR, success,
            success ? "更新头像成功" : "更新头像失败");
    }

    /**
     * 上传用户头像
     * @param userId 用户ID
     * @param file 头像文件
     * @return 上传结果
     */
    @PostMapping("/{userId}/avatar/upload")
    public Result uploadAvatar(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return new Result(Code.SAVE_ERR, null, "只能上传图片文件");
            }

            // 检查文件大小（额外检查，虽然配置中已经限制了）
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                return new Result(Code.SAVE_ERR, null, "文件大小不能超过10MB");
            }

            // 上传文件
            String avatarUrl = fileUploadUtil.uploadFile(file);

            // 更新用户头像
            User user = userService.getById(userId);
            if (user == null) {
                return new Result(Code.GET_ERR, null, "用户不存在");
            }
            
            user.setAvatar(avatarUrl);
            boolean success = userService.updateById(user);
            return new Result(success ? Code.UPDATE_OK : Code.UPDATE_ERR, avatarUrl,
                success ? "头像上传成功" : "头像上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Code.SAVE_ERR, null, "文件上传失败：" + e.getMessage());
        }
    }
}
