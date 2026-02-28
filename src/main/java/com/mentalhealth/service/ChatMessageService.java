package com.mentalhealth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.entity.ChatMessage;
import com.mentalhealth.dto.ConversationDto;
import com.mentalhealth.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天消息服务类
 */
@Service
public class ChatMessageService extends ServiceImpl<ChatMessageMapper, ChatMessage> {

    /**
     * 保存消息
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param content 内容
     * @param type 类型
     * @return 保存后的消息
     */
    public ChatMessage saveMessage(Long senderId, Long receiverId, String content, String type) {
        ChatMessage message = new ChatMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setType(type != null ? type : "text");
        message.setCreateTime(LocalDateTime.now());
        message.setIsRead(false);
        
        this.save(message);
        return message;
    }

    /**
     * 获取两人之间的历史消息
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 消息列表
     */
    public List<ChatMessage> getHistoryMessages(Long userId1, Long userId2) {
        QueryWrapper<ChatMessage> query = new QueryWrapper<>();
        query.and(wrapper -> wrapper
                .eq("sender_id", userId1).eq("receiver_id", userId2)
                .or()
                .eq("sender_id", userId2).eq("receiver_id", userId1)
        );
        query.orderByAsc("create_time");
        return this.list(query);
    }
    
    public List<ConversationDto> getConversations(Long userId) {
        QueryWrapper<ChatMessage> query = new QueryWrapper<>();
        query.and(w -> w.eq("sender_id", userId).or().eq("receiver_id", userId));
        query.orderByDesc("create_time");
        List<ChatMessage> messages = this.list(query);
        Map<Long, ConversationDto> map = new LinkedHashMap<>();
        for (ChatMessage msg : messages) {
            Long partnerId = msg.getSenderId().equals(userId) ? msg.getReceiverId() : msg.getSenderId();
            if (!map.containsKey(partnerId)) {
                ConversationDto dto = new ConversationDto();
                dto.setPartnerId(partnerId);
                dto.setLastMessage(msg);
                map.put(partnerId, dto);
            }
        }
        return new ArrayList<>(map.values());
    }
}
