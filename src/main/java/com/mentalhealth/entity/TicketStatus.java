package com.mentalhealth.entity;

public enum TicketStatus {
    PENDING("待受理"),
    IN_PROGRESS("咨询中"),
    COMPLETED("已完成"),
    CLOSED("已关闭");

    private final String description;

    TicketStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    /**
     * 根据字符串值获取枚举
     * @param value 字符串值
     * @return 对应的枚举值
     */
    public static TicketStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return PENDING;
        }
        
        for (TicketStatus status : TicketStatus.values()) {
            if (status.name().equalsIgnoreCase(value) || 
                status.name().toLowerCase().equals(value.toLowerCase())) {
                return status;
            }
        }
        
        // 尝试匹配数据库中的字符串格式
        switch (value.toLowerCase()) {
            case "pending":
                return PENDING;
            case "in_progress":
                return IN_PROGRESS;
            case "completed":
                return COMPLETED;
            case "closed":
                return CLOSED;
            default:
                return PENDING; // 默认返回PENDING
        }
    }
}