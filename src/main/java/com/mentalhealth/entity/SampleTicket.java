package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sample_ticket")
public class SampleTicket {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("ticket_no")
    private String ticketNo;
    
    @TableField("title")
    private String title;
    
    @TableField("content")
    private String content;
    
    @TableField("status")
    private String status = "PENDING";
    
    @TableField("category")
    private String category;
    
    @TableField("priority")
    private String priority = "MEDIUM";
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("counselor_id")
    private Long counselorId;
    
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime = LocalDateTime.now();
    
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime = LocalDateTime.now();
    
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted = false;
}