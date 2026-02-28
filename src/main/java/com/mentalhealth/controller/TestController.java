package com.mentalhealth.controller;

import com.mentalhealth.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/test/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Hello World!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/test/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "注册测试成功");
        response.put("receivedData", request);
        return ResponseEntity.ok(response);
    }
}