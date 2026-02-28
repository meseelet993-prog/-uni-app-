package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("phone")
    private String phone;
    
    // 初始化时就设置默认值
    @TableField("role")
    private String role = "student";
    
    @TableField("status")
    private Integer status;
    
    @TableField("real_name")
    private String realName;
    
    @TableField("email")
    private String email;
    
    @TableField("avatar_url")
    private String avatarUrl;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    // 确保getRole()方法不会返回null
    public String getRole() {
        return role == null ? "student" : role;
    }
    
    // 确保setRole()方法处理null值
    public void setRole(String role) {
        this.role = (role == null || role.isEmpty()) ? "student" : role;
    }
}