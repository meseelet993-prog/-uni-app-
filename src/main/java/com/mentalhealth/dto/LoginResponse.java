package com.mentalhealth.dto;

import com.mentalhealth.entity.User;
import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    /**
     * 用户信息
     */
    private User user;
    
    /**
     * 认证令牌
     */
    private String token;
    
    /**
     * 登录角色
     */
    private String loginRole;
}