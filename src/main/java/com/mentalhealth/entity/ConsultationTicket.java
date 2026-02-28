package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("consultation_ticket")
public class ConsultationTicket {
    
    /**
     * 主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 工单编号，可生成唯一标识
     */
    @TableField("ticket_no")
    private String ticketNo;
    
    /**
     * 学生ID，关联用户表
     */
    @TableField("student_id")
    private Long studentId;
    
    /**
     * 咨询师ID，可为空（未分配时）
     */
    @TableField("counselor_id")
    private Long counselorId;
    
    /**
     * 工单标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 工单内容（咨询问题描述）
     */
    @TableField("content")
    private String content;
    
    /**
     * 状态：pending（待受理）、accepted（已受理）、in_progress（咨询中）、completed（已完成）、closed（已关闭）
     */
    @TableField("status")
    private String status;
    
    /**
     * 优先级：low/medium/high
     */
    @TableField("priority")
    private String priority;
    
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