package top.sheepspace.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    /**
     * 上传文件
     * @param file 上传的文件
     * @return 文件名（包含扩展名）
     * @throws IOException 如果文件上传失败
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 生成唯一的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID().toString() + extension;
        
        // 保存文件
        File destFile = new File(uploadDir.getAbsolutePath() + File.separator + filename);
        file.transferTo(destFile);
        
        // 只返回文件名
        return filename;
    }
} 