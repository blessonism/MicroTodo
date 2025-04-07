package top.sheepspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import top.sheepspace.pojo.User;
import top.sheepspace.service.IUserService;
import top.sheepspace.utils.JwtUtil;
import top.sheepspace.controller.Result;
import top.sheepspace.controller.Code;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            String displayName = request.get("displayName");

            // 验证必填字段
            if (username == null || password == null || email == null) {
                return new Result(Code.SAVE_ERR, null, "用户名、密码和邮箱为必填项");
            }

            // 检查用户名是否已存在
            if (userService.lambdaQuery().eq(User::getUname, username).one() != null) {
                return new Result(Code.SAVE_ERR, null, "用户名已存在");
            }

            // 检查邮箱是否已存在
            if (userService.lambdaQuery().eq(User::getEmail, email).one() != null) {
                return new Result(Code.SAVE_ERR, null, "邮箱已被注册");
            }

            // 创建新用户
            User user = new User();
            user.setUname(username);
            user.setPassword(passwordEncoder.encode(password)); // 加密密码
            user.setEmail(email);
            user.setDisplayName(displayName != null ? displayName : username);

            userService.save(user);

            // 生成JWT token
            String token = jwtUtil.generateToken(username);

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return new Result(Code.SAVE_OK, data, "注册成功");
        } catch (Exception e) {
            return new Result(Code.SYSTEM_ERR, null, "系统错误：" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> request) {
        try {
            // 支持 uname 或 email 字段
            final String usernameOrEmail = request.get("uname") != null ? 
                request.get("uname") : request.get("email");
            final String password = request.get("password");

            // 验证必填字段
            if (usernameOrEmail == null || password == null) {
                return new Result(Code.GET_ERR, null, "用户名/邮箱和密码为必填项");
            }

            // 查找用户（支持用户名或邮箱登录）
            User user = userService.lambdaQuery()
                    .and(wrapper -> wrapper
                        .eq(User::getUname, usernameOrEmail)
                        .or()
                        .eq(User::getEmail, usernameOrEmail))
                        .one();

            if (user == null) {
                return new Result(Code.GET_ERR, null, "用户不存在");
            }

            // 验证密码（支持加密密码和明文密码）
            boolean passwordMatch = password.equals(user.getPassword()) || 
                                  passwordEncoder.matches(password, user.getPassword());
            
            if (!passwordMatch) {
                return new Result(Code.GET_ERR, null, "密码错误");
            }

            // 生成JWT token
            String token = jwtUtil.generateToken(user.getUname());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return new Result(Code.GET_OK, data, "登录成功");
        } catch (Exception e) {
            return new Result(Code.SYSTEM_ERR, null, "系统错误：" + e.getMessage());
        }
    }
/**
     * 保存 GitHub 登录的 state 参数到 Session
     */
    @PostMapping("/github/state")
    public Result saveGithubState(@RequestBody Map<String, String> request, HttpSession session) {
        String state = request.get("state");
        if (state == null || state.isEmpty()) {
            return new Result(Code.GET_ERR, null, "State 不能为空");
        }
        session.setAttribute("GITHUB_STATE", state);
        return new Result(Code.GET_OK, null, "State 保存成功");
    }

    /**
     * GitHub 登录回调处理
     */
    @GetMapping("/github/callback")
    public Result githubCallback(HttpServletRequest request) {
        try {
            System.out.println("\n=== 处理GitHub回调 ===");
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Query string: " + request.getQueryString());

            // 获取回调参数
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            System.out.println("Code: " + code);
            System.out.println("State: " + state);

            // 校验参数
            if (code == null || state == null) {
                return new Result(Code.SYSTEM_ERR, null, "GitHub回调参数缺失");
            }

            // 验证 state
            String sessionState = (String) request.getSession().getAttribute("GITHUB_STATE");
            if (sessionState == null || !sessionState.equals(state)) {
                return new Result(Code.SYSTEM_ERR, null, "State 验证失败，请重新登录");
            }
            System.out.println("State 验证成功");
            request.getSession().removeAttribute("GITHUB_STATE"); // 清除 state

            // 使用 code 获取 access token
            String accessToken = getAccessTokenFromGitHub(code, state);
            if (accessToken == null) {
                return new Result(Code.SYSTEM_ERR, null, "获取 GitHub 访问令牌失败");
            }

            // 使用 access token 获取 GitHub 用户信息
            Map<String, Object> userInfo = getGithubUserInfo(accessToken);
            if (userInfo == null) {
                return new Result(Code.SYSTEM_ERR, null, "获取 GitHub 用户信息失败");
            }

            // 处理用户信息
            String githubId = userInfo.get("id").toString();
            String login = (String) userInfo.get("login");
            String email = (String) userInfo.get("email");
            String avatarUrl = (String) userInfo.get("avatar_url");
            String name = (String) userInfo.get("name");

            if (email == null) {
                // 处理邮箱未公开的情况，例如提示用户公开邮箱或使用其他方式获取邮箱
                System.out.println("用户未公开邮箱地址。");
            }

            // 创建或更新用户
            User user = userService.createOrUpdateGithubUser(githubId, login, email, avatarUrl, name);

            // 生成 JWT token
            String token = jwtUtil.generateToken(user.getUname());

            // 返回用户信息和 token
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);

            return new Result(Code.GET_OK, data, "GitHub 登录成功");
        } catch (Exception e) {
            System.out.println("\n=== GitHub回调处理时出错 ===");
            e.printStackTrace();
            return new Result(Code.SYSTEM_ERR, null, "GitHub登录处理失败：" + e.getMessage());
        }
    }

    /**
     * 从 GitHub 获取 access token
     */
    private String getAccessTokenFromGitHub(String code, String state) {
        try {
            RestTemplate  restTemplate = new RestTemplate();
            String tokenUrl = "https://github.com/login/oauth/access_token";

            // 准备请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 检查环境变量
            if (clientId == null || clientSecret == null) {
                throw new IllegalArgumentException("GITHUB_CLIENT_ID 或 GITHUB_CLIENT_SECRET 环境变量未配置");
            }

            // 准备请求体
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSecret);
            requestBody.add("code", code);
            requestBody.add("state", state);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求获取 access token
            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                if (responseBody != null) {
                    // 解析响应body，提取access_token
                    String[] params = responseBody.split("&");
                    for (String param : params) {
                        if (param.startsWith("access_token=")) {
                            return param.split("=")[1];
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("获取 GitHub 访问令牌时发生错误: " + e.getMessage());
        }
        return null;
    }


    /**
     * 使用 access token 获取 GitHub 用户信息
     */
    private Map<String, Object> getGithubUserInfo(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String userInfoUrl = "https://api.github.com/user";

            // 准备请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            // 发送请求获取用户信息
            ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                requestEntity,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userInfo = response.getBody();
                if (userInfo != null) {
                    String email = (String) userInfo.get("email");
                    if (email != null) {
                        System.out.println("Email: " + email);
                    } else {
                        System.out.println("User email is not available.");
                    }
                }
                return response.getBody();
            }
        } catch (Exception e) {
            System.out.println("获取 GitHub 用户信息时出错: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/github/bind/{username}")
    public Result initiateGithubBinding(@PathVariable String username) {
        User user = userService.lambdaQuery().eq(User::getUname, username).one();
        if (user == null) {
            return new Result(Code.GET_ERR, null, "用户不存在");
        }
        
        // 生成绑定URL，state参数包含当前用户名
        String bindUrl = "/oauth2/authorization/github?state=bind_" + username;
        Map<String, Object> data = new HashMap<>();
        data.put("bindUrl", bindUrl);
        
        return new Result(Code.GET_OK, data, "获取绑定URL成功");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(principal.getAttributes());
    }

    /**
     * 更新用户密码为加密形式
     * 注意：此方法仅用于开发环境，生产环境应该移除
     */
    @PostMapping("/update-password")
    public Result updatePassword(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");

            User user = userService.lambdaQuery()
                    .eq(User::getUname, username)
                    .one();

            if (user == null) {
                return new Result(Code.GET_ERR, null, "用户不存在");
            }

            // 验证旧密码
            if (!oldPassword.equals(user.getPassword())) {
                return new Result(Code.GET_ERR, null, "旧密码错误");
            }

            // 更新为加密密码
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateById(user);

            return new Result(Code.UPDATE_OK, null, "密码更新成功");
        } catch (Exception e) {
            return new Result(Code.SYSTEM_ERR, null, "系统错误：" + e.getMessage());
        }
    }

    /**
     * 批量更新所有用户的明文密码为加密形式
     * 注意：此方法仅用于开发环境，生产环境应该移除
     */
    @PostMapping("/encrypt-all-passwords")
    public Result encryptAllPasswords() {
        try {
            List<User> users = userService.list();
            for (User user : users) {
                // 如果密码看起来不像已经加密的形式
                if (!user.getPassword().startsWith("$2a$")) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userService.updateById(user);
                }
            }
            return new Result(Code.UPDATE_OK, null, "所有密码已更新为加密形式");
        } catch (Exception e) {
            return new Result(Code.SYSTEM_ERR, null, "系统错误：" + e.getMessage());
        }
    }

    @PostMapping("/github/login")
    public Result githubLogin(@RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            String state = request.get("state");

            System.out.println("Processing GitHub login");
            System.out.println("Code: " + code);
            System.out.println("State: " + state);

            if (code == null) {
                return new Result(Code.SYSTEM_ERR, null, "未收到GitHub授权码");
            }

            // 使用 code 获取 access token
            RestTemplate restTemplate = new RestTemplate();
            
            // 准备获取 access token 的请求参数
            String tokenUrl = "https://github.com/login/oauth/access_token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("client_id", System.getenv("GITHUB_CLIENT_ID"));
            requestBody.put("client_secret", System.getenv("GITHUB_CLIENT_SECRET"));
            requestBody.put("code", code);
            requestBody.put("state", state);
            
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 发送请求获取 access token
            ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
            );
            
            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                return new Result(Code.SYSTEM_ERR, null, "获取GitHub访问令牌失败");
            }
            
            String accessToken = (String) tokenResponse.getBody().get("access_token");
            if (accessToken == null) {
                return new Result(Code.SYSTEM_ERR, null, "GitHub访问令牌无效");
            }
            
            // 使用 access token 获取用户信息
            String userInfoUrl = "https://api.github.com/user";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<?> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders);
            
            ResponseEntity<Map> userResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userInfoRequestEntity,
                Map.class
            );
            
            if (!userResponse.getStatusCode().is2xxSuccessful()) {
                return new Result(Code.SYSTEM_ERR, null, "获取GitHub用户信息失败");
            }
            
            Map<String, Object> userInfo = userResponse.getBody();
            
            // 处理用户信息
            String githubId = userInfo.get("id").toString();
            String login = (String) userInfo.get("login");
            String email = (String) userInfo.get("email");
            String avatarUrl = (String) userInfo.get("avatar_url");
            String name = (String) userInfo.get("name");
            
            // 创建或更新用户
            User user = userService.createOrUpdateGithubUser(githubId, login, email, avatarUrl, name);
            
            // 生成 JWT token
            String token = jwtUtil.generateToken(user.getUname());
            
            // 返回用户信息和token
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            
            return new Result(Code.GET_OK, data, "GitHub登录成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Code.SYSTEM_ERR, null, "GitHub登录失败：" + e.getMessage());
        }
    }
    
    private String extractAccessToken(String response) {
        // access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&scope=repo%2Cgist&token_type=bearer
        String[] pairs = response.split("&");
        for (String pair : pairs) {
            if (pair.startsWith("access_token=")) {
                return pair.substring("access_token=".length());
            }
        }
        return null;
    }

    @GetMapping("/github/authorize")
    public Result getGithubAuthorizationUrl(@RequestParam String state) {
        try {
            String redirectUri = "http://localhost:8080/auth/github/callback";
            String scope = "read:user user:email";
            
            // 直接使用 Spring Security OAuth2 的授权端点
            String authorizationUrl = String.format(
                "/oauth2/authorization/github?state=%s",
                URLEncoder.encode(state, "UTF-8")
            );
            
            Map<String, String> data = new HashMap<>();
            data.put("authorizationUrl", authorizationUrl);
            
            return new Result(Code.GET_OK, data, "获取GitHub授权URL成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Code.SYSTEM_ERR, null, "获取GitHub授权URL失败：" + e.getMessage());
        }
    }
} 