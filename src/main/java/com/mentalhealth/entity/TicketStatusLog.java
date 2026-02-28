package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ticket_status_log")
public class TicketStatusLog {
    
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
     * 原状态
     */
    @TableField("old_status")
    private String oldStatus;
    
    /**
     * 新状态
     */
    @TableField("new_status")
    private String newStatus;
    
    /**
     * 操作人ID
     */
    @TableField("operator_id")
    private Long operatorId;
    
    /**
     * 操作人角色（student/counselor/admin）
     */
    @TableField("operator_role")
    private String operatorRole;
    
    /**
     * 操作备注（如：受理原因、关闭原因等）
     */
    @TableField("remark")
    private String remark;
    
    /**
     * 操作时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}