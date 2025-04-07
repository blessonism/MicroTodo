package top.sheepspace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/test")
public class FileTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileTestController.class);
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @GetMapping("/files")
    public String listFiles() {
        try {
            Path path = Paths.get(uploadPath).toAbsolutePath().normalize();
            File dir = path.toFile();
            
            if (!dir.exists() || !dir.isDirectory()) {
                return "Upload directory does not exist: " + dir.getAbsolutePath();
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("Upload directory: ").append(dir.getAbsolutePath()).append("\n");
            sb.append("Files:\n");
            
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    sb.append(" - ").append(file.getName())
                      .append(" (").append(file.length()).append(" bytes)")
                      .append(" [").append(file.canRead() ? "R" : "-").append(file.canWrite() ? "W" : "-").append("]\n");
                }
            }
            
            return sb.toString();
        } catch (Exception e) {
            logger.error("Error listing files", e);
            return "Error: " + e.getMessage();
        }
    }
    
    @GetMapping("/file/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path path = Paths.get(uploadPath, filename).toAbsolutePath().normalize();
            File file = path.toFile();
            
            if (!file.exists() || !file.canRead()) {
                logger.error("File not found or not readable: {}", path);
                return ResponseEntity.notFound().build();
            }
            
            FileSystemResource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error serving file: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 