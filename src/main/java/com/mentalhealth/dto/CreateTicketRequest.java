package com.mentalhealth.dto;

import lombok.Data;

@Data
public class CreateTicketRequest {
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 工单标题
     */
    private String title;
    
    /**
     * 工单内容
     */
    private String content;
    
    /**
     * 工单分类
     */
    private String category;
    
    /**
     * 优先级
     */
    private String priority;
    
    /**
     * 是否匿名提交
     */
    private Boolean isAnonymous = false;
    
    /**
     * 附件列表
     */
    private String[] attachments;
}