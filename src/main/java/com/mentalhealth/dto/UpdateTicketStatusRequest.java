package com.mentalhealth.dto;

import lombok.Data;

@Data
public class UpdateTicketStatusRequest {
    /**
     * 新状态
     */
    private String status;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人角色
     */
    private String operatorRole;
    
    /**
     * 操作备注
     */
    private String remark;
}