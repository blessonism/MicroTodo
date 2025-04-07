package top.sheepspace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            try {
                OAuth2User user = delegate.loadUser(userRequest);
                System.out.println("OAuth2 User Service - Access Token: " + userRequest.getAccessToken().getTokenValue());
                System.out.println("OAuth2 User Service - User Attributes: " + user.getAttributes());
                return user;
            } catch (OAuth2AuthenticationException e) {
                System.out.println("OAuth2 Authentication Exception: " + e.getMessage());
                throw e;
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .cors()
            .and()
        .csrf()
            .disable()
        .authorizeRequests()
            .antMatchers("/**")
                .permitAll()
        .and()
        .oauth2Login()
            .defaultSuccessUrl("/auth/github/callback")
            .userInfoEndpoint()
                .userService(oauth2UserService())
            .and()
            .successHandler((request, response, authentication) -> {
                System.out.println("\n=== OAuth2 认证成功 ===");
                System.out.println("Request URI: " + request.getRequestURI());
                System.out.println("Session ID: " + request.getSession().getId());
                System.out.println("Authentication class: " + authentication.getClass().getName());
                
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                System.out.println("OAuth2User class: " + oauth2User.getClass().getName());
                System.out.println("OAuth2User attributes: " + oauth2User.getAttributes());
                
                // 确保创建新的session
                HttpSession session = request.getSession(true);
                session.setAttribute("OAUTH2_USER", oauth2User);
                System.out.println("Saved OAuth2User to session: " + session.getId());
                
                // 获取原始的 state 参数
                String state = request.getParameter("state");
                System.out.println("Original state parameter: " + state);
                
                // 构建回调URL，包含session id
                String callbackUrl = String.format("/auth/github/callback?state=%s&sessionId=%s", 
                    state, session.getId());
                System.out.println("Redirecting to: " + callbackUrl);
                response.sendRedirect(callbackUrl);
            })
            .failureHandler((request, response, exception) -> {
                System.out.println("\n=== OAuth2 认证失败 ===");
                System.out.println("Request URI: " + request.getRequestURI());
                System.out.println("Session ID: " + request.getSession().getId());
                System.out.println("Error class: " + exception.getClass().getName());
                System.out.println("Error message: " + exception.getMessage());
                exception.printStackTrace();
                
                response.sendRedirect("/login?error=github_auth_failed");
            });
    }
} 