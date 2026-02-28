package com.mentalhealth.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mentalhealth.entity.ChatMessage;
import com.mentalhealth.service.ChatMessageService;
import com.mentalhealth.service.UserService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务端端点
 */
@Slf4j
@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocketServer {

    /**
     * 在线会话管理器 (UserId -> Session)
     */
    private static final Map<Long, Session> onlineSessions = new ConcurrentHashMap<>();

    /**
     * 静态注入Service，解决@ServerEndpoint无法自动注入的问题
     */
    private static ChatMessageService chatMessageService;
    private static UserService userService;
    
    @Autowired
    public void setChatMessageService(ChatMessageService chatMessageService) {
        ChatWebSocketServer.chatMessageService = chatMessageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        ChatWebSocketServer.userService = userService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        if (userId == null) {
            closeSession(session, "User ID is required");
            return;
        }

        // 验证用户是否存在
        if (userService.getById(userId) == null) {
            closeSession(session, "User not found");
            return;
        }

        // 这里可以添加Token验证逻辑，例如从QueryString中获取Token并验证
        // String queryString = session.getQueryString();
        // verifyToken(queryString);

        onlineSessions.put(userId, session);
        log.info("用户[{}]连接成功，当前在线人数: {}", userId, onlineSessions.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        onlineSessions.remove(userId);
        log.info("用户[{}]断开连接，当前在线人数: {}", userId, onlineSessions.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 消息格式示例: {"receiverId": 2, "content": "Hello", "type": "text"}
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Long userId) {
        log.info("收到用户[{}]的消息: {}", userId, message);
        
        try {
            // 1. 解析消息
            JSONObject jsonObject = JSON.parseObject(message);
            Long receiverId = jsonObject.getLong("receiverId");
            String content = jsonObject.getString("content");
            String type = jsonObject.getString("type");
            
            if (receiverId == null || content == null) {
                log.warn("消息格式错误，缺少必要字段");
                return;
            }
            
            // 2. 保存消息到数据库
            ChatMessage savedMsg = chatMessageService.saveMessage(userId, receiverId, content, type);
            
            // 3. 查找接收者会话并转发
            Session receiverSession = onlineSessions.get(receiverId);
            if (receiverSession != null && receiverSession.isOpen()) {
                // 转发完整的消息对象（包含ID和时间）
                receiverSession.getBasicRemote().sendText(JSON.toJSONString(savedMsg));
                log.info("消息已转发给用户[{}]", receiverId);
            } else {
                log.info("用户[{}]不在线，消息已保存", receiverId);
            }
            
        } catch (Exception e) {
            log.error("处理消息异常", e);
            try {
                session.getBasicRemote().sendText("{\"error\": \"消息处理失败: " + e.getMessage() + "\"}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误", error);
    }
    
    private void closeSession(Session session, String reason) {
        try {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, reason));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
