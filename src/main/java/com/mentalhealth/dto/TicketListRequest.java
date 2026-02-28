package com.mentalhealth.dto;

import lombok.Data;

@Data
public class TicketListRequest {
    /**
     * 当前页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    /**
     * 角色过滤
     */
    private String role;
    
    /**
     * 用户ID
     */
    private Long userId;
}