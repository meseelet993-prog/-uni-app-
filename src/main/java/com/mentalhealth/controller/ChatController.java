package com.mentalhealth.controller;

import com.mentalhealth.dto.ApiResponse;
import com.mentalhealth.entity.ChatMessage;
import com.mentalhealth.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天相关接口
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 获取与指定用户的聊天历史记录
     * @param userId1 当前用户ID
     * @param userId2 对方用户ID
     * @return 聊天记录列表
     */
    @GetMapping("/history")
    public ApiResponse<List<ChatMessage>> getHistoryMessages(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        
        List<ChatMessage> messages = chatMessageService.getHistoryMessages(userId1, userId2);
        ApiResponse<List<ChatMessage>> response = ApiResponse.success(messages);
        response.setMessage("获取聊天记录成功");
        return response;
    }
}
