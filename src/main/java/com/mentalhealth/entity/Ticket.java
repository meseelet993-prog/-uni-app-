package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ticket")
public class Ticket {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 工单编号
     */
    @TableField("ticket_no")
    private String ticketNo;
    
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 内容
     */
    @TableField("content")
    private String content;
    
    /**
     * 分类
     */
    @TableField("category")
    private String category;
    
    /**
     * 状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 优先级
     */
    @TableField("priority")
    private String priority;
    
    /**
     * 学生ID
     */
    @TableField("student_id")
    private Long studentId;
    
    /**
     * 咨询师ID
     */
    @TableField("counselor_id")
    private Long counselorId;
    
    /**
     * 咨询师姓名
     */
    @TableField("counselor_name")
    private String counselorName;
    
    /**
     * 是否匿名
     */
    @TableField("is_anonymous")
    private Boolean isAnonymous = false;
    
    /**
     * 附件列表（JSON格式存储）
     */
    @TableField("attachments")
    private String attachments;

    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 获取状态枚举
     */
    public TicketStatus getStatusEnum() {
        try {
            // 尝试将数据库中的字符串转换为枚举
            return TicketStatus.fromString(this.status);
        } catch (Exception e) {
            // 如果转换失败，返回默认状态
            return TicketStatus.PENDING;
        }
    }

    /**
     * 设置状态枚举
     */
    public void setStatusEnum(TicketStatus status) {
        this.status = status != null ? status.name().toLowerCase() : null;
    }
    
    /**
     * 获取分类枚举
     */
    public TicketCategory getCategoryEnum() {
        try {
            return TicketCategory.fromString(this.category);
        } catch (Exception e) {
            // 如果转换失败，返回默认分类
            return TicketCategory.OTHER;
        }
    }

    /**
     * 设置分类枚举
     */
    public void setCategoryEnum(TicketCategory category) {
        this.category = category != null ? category.name() : null;
    }
    
    /**
     * 获取优先级枚举
     */
    public TicketPriority getPriorityEnum() {
        try {
            return TicketPriority.fromString(this.priority);
        } catch (Exception e) {
            // 如果转换失败，返回默认优先级
            return TicketPriority.MEDIUM;
        }
    }

    /**
     * 设置优先级枚举
     */
    public void setPriorityEnum(TicketPriority priority) {
        this.priority = priority != null ? priority.name() : null;
    }
}