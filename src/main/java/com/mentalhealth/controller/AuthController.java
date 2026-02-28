package com.mentalhealth.controller;

import com.mentalhealth.dto.ApiResponse;
import com.mentalhealth.dto.LoginRequest;
import com.mentalhealth.dto.LoginResponse;
import com.mentalhealth.dto.RegisterRequest;
import com.mentalhealth.entity.User;
import com.mentalhealth.service.AuthService;
import com.mentalhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册等相关接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * @param request 登录请求参数，包括用户名、密码和角色
     * @return 登录结果
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(401, e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 转换为User对象
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setRole(request.getRole());

            // 基本验证
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "用户名不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            User newUser = userService.register(user);
            response.put("code", 200);
            response.put("message", "注册成功");
            response.put("data", newUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.getUserById(id);
            if (user != null) {
                // 确保角色字段不为空
                if (user.getRole() == null || user.getRole().isEmpty()) {
                    user.setRole("student");
                }
                
                response.put("code", 200);
                response.put("message", "获取用户信息成功");
                response.put("data", user);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "用户不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "服务器错误: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}