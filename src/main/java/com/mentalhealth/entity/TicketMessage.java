package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ticket_message")
public class TicketMessage {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 关联工单ID
     */
    @TableField("ticket_id")
    private Long ticketId;
    
    /**
     * 发送者ID
     */
    @TableField("sender_id")
    private Long senderId;
    
    /**
     * 发送者角色（student/counselor/admin）
     */
    @TableField("sender_role")
    private String senderRole;
    
    /**
     * 消息内容
     */
    @TableField("content")
    private String content;
    
    /**
     * 消息类型（text/image/file等）
     */
    @TableField("message_type")
    private String messageType = "text";
    
    /**
     * 是否已读
     */
    @TableField("is_read")
    private Boolean isRead = false;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    

}