package com.mentalhealth.controller;

import com.mentalhealth.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("")
public class FileUploadController {
    
    // 文件存储目录
    private static final String UPLOAD_DIR = "uploads/";
    
    /**
     * 文件上传接口
     * 支持图片、文档上传，限制文件大小10MB
     * @param file 上传的文件
     * @return 上传结果和文件信息
     */
    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ApiResponse.error(400, "上传文件不能为空");
            }
            
            // 检查文件大小（限制10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return ApiResponse.error(400, "文件大小不能超过10MB");
            }
            
            // 检查文件类型（允许的类型）
            String contentType = file.getContentType();
            if (contentType == null || 
                (!contentType.startsWith("image/") && 
                 !contentType.equals("application/pdf") &&
                 !contentType.equals("application/msword") &&
                 !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") &&
                 !contentType.equals("application/vnd.ms-excel") &&
                 !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") &&
                 !contentType.equals("text/plain"))) {
                return ApiResponse.error(400, "不支持的文件类型");
            }
            
            // 创建上传目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // 保存文件
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.write(path, bytes);
            
            return ApiResponse.success(uniqueFilename);
        } catch (IOException e) {
            return ApiResponse.error(500, "文件上传失败: " + e.getMessage());
        }
    }
}