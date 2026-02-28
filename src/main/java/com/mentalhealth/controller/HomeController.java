package com.mentalhealth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "欢迎使用心理健康平台 API");
        result.put("version", "1.0.0");
        return result;
    }
    
    @GetMapping("/api")
    public Map<String, Object> apiHome() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "心理健康平台 API 根路径");
        result.put("endpoints", new String[]{
            "GET /api/test - 测试接口",
            "POST /api/auth/login - 用户登录",
            "POST /api/auth/register - 用户注册",
            "GET /api/auth/user/{id} - 获取用户信息"
        });
        return result;
    }
}