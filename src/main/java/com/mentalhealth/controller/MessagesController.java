package com.mentalhealth.controller;

import com.mentalhealth.dto.ApiResponse;
import com.mentalhealth.dto.ConversationDto;
import com.mentalhealth.entity.User;
import com.mentalhealth.service.ChatMessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/conversations")
    public ApiResponse<List<ConversationDto>> getConversations(@RequestParam(required = false) Long userId, HttpServletRequest request) {
        if (userId == null) {
            // 尝试从请求属性中获取当前登录用户
            User currentUser = (User) request.getAttribute("currentUser");
            if (currentUser != null) {
                userId = currentUser.getId();
            }
        }
        
        if (userId == null) {
            return ApiResponse.error(400, "userId parameter is required");
        }
        List<ConversationDto> list = chatMessageService.getConversations(userId);
        return ApiResponse.success(list);
    }
}
