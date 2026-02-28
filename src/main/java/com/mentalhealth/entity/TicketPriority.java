package com.mentalhealth.entity;

public enum TicketPriority {
    LOW("低"),
    MEDIUM("中"),
    HIGH("高");

    private final String description;

    TicketPriority(String description) {
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
    public static TicketPriority fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return MEDIUM;
        }
        
        for (TicketPriority priority : TicketPriority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        
        // 尝试匹配数据库中的字符串格式（大写）
        switch (value.toUpperCase()) {
            case "LOW":
                return LOW;
            case "MEDIUM":
                return MEDIUM;
            case "HIGH":
                return HIGH;
            default:
                return MEDIUM; // 默认返回MEDIUM
        }
    }
}