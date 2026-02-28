package com.mentalhealth.service;

import com.mentalhealth.dto.LoginRequest;
import com.mentalhealth.dto.LoginResponse;
import com.mentalhealth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证服务类
 */
@Service
public class AuthService {

    @Autowired
    private UserService userService;

    /**
     * 用户登录验证
     * @param request 登录请求包含用户名、密码和角色
     * @return 登录响应
     * @throws RuntimeException 当验证失败时抛出异常
     */
    public LoginResponse login(LoginRequest request) throws RuntimeException {
        String username = request.getUsername();
        String password = request.getPassword();
        String requestRole = request.getRole();
        
        System.out.println("登录验证 - 用户名: " + username + ", 请求角色: " + requestRole);
        
        // 1. 验证用户名和密码
        User user = userService.login(username, password);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 2. 【关键修改】不再验证角色是否匹配，而是直接使用前端传递的角色作为本次登录的角色
        String loginRole = requestRole;
        
        // 3. 验证角色的合法性（必须是系统支持的角色）
        List<String> validRoles = Arrays.asList("STUDENT", "CONSULTANT", "ADMIN");
        if (loginRole == null || !validRoles.contains(loginRole.toUpperCase())) {
            throw new RuntimeException("无效的角色选择");
        }
        
        // 4. 构建响应对象
        LoginResponse response = new LoginResponse();
        
        // 创建一个新的User对象用于响应，确保所有字段都有合适的默认值
        User responseUser = new User();
        responseUser.setId(user.getId());
        responseUser.setUsername(user.getUsername());
        responseUser.setPassword(""); // 不在响应中包含密码
        responseUser.setPhone(user.getPhone());
        responseUser.setRole(loginRole); // 使用前端选择的角色而不是数据库中的角色
        responseUser.setStatus(user.getStatus());
        responseUser.setRealName(user.getRealName());
        responseUser.setEmail(user.getEmail());
        responseUser.setAvatarUrl(user.getAvatarUrl());
        responseUser.setCreatedAt(user.getCreatedAt());
        
        response.setUser(responseUser);
        response.setLoginRole(loginRole); // 明确返回登录角色
        // 这里可以添加token生成逻辑
        
        System.out.println("登录成功，返回用户角色: " + responseUser.getRole());
        return response;
    }
}