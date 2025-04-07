package top.sheepspace.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Value("${file.access.path}")
    private String accessPath;
    
    @PostConstruct
    public void init() {
        try {
            // 获取绝对路径
            Path path = Paths.get(uploadPath).toAbsolutePath().normalize();
            File uploadDir = path.toFile();
            
            // 确保上传目录存在
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                logger.info("Created upload directory: {} - Success: {}", uploadDir.getAbsolutePath(), created);
            }
            
            // 检查目录权限
            boolean canRead = uploadDir.canRead();
            boolean canWrite = uploadDir.canWrite();
            logger.info("Upload directory permissions - Read: {}, Write: {}", canRead, canWrite);
            
            // 记录目录信息
            logger.info("Upload path (absolute): {}", uploadDir.getAbsolutePath());
            logger.info("Access path: {}", accessPath);
            
            // 列出目录中的文件
            File[] files = uploadDir.listFiles();
            if (files != null) {
                logger.info("Files in upload directory:");
                for (File file : files) {
                    logger.info(" - {} ({}bytes)", file.getName(), file.length());
                }
            }
        } catch (Exception e) {
            logger.error("Error initializing upload directory", e);
        }
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // 获取绝对路径
            Path path = Paths.get(uploadPath).toAbsolutePath().normalize();
            String location = "file:" + path.toString() + File.separator;
            
            logger.info("Adding resource handler mapping: {} -> {}", accessPath + "**", location);
                
            // 添加新的映射路径
            registry.addResourceHandler("/uploads/avatars/**")
                    .addResourceLocations(location)
                    .setCachePeriod(3600)
                    .resourceChain(true);
                    
            logger.info("Resource handler configured successfully");
        } catch (Exception e) {
            logger.error("Error configuring resource handler", e);
        }
    }
} 