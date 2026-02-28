package com.mentalhealth.dto;

import lombok.Data;

@Data
public class CreateTicketMessageRequest {
    /**
     * 发送者ID
     */
    private Long senderId;
    
    /**
     * 发送者角色
     */
    private String senderRole;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型
     */
    private String messageType = "text";
}