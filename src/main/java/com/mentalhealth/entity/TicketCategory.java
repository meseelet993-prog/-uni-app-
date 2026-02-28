package com.mentalhealth.entity;

public enum TicketCategory {
    STUDY("学习压力"),
    RELATIONSHIP("人际关系"),
    EMOTION("情绪问题"),
    CAREER("职业规划"),
    FAMILY("家庭关系"),
    LOVE("恋爱问题"),
    OTHER("其他问题");

    private final String description;

    TicketCategory(String description) {
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
    public static TicketCategory fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }
        
        for (TicketCategory category : TicketCategory.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        
        // 尝试匹配数据库中的字符串格式（大写）
        switch (value.toUpperCase()) {
            case "STUDY":
                return STUDY;
            case "RELATIONSHIP":
                return RELATIONSHIP;
            case "EMOTION":
                return EMOTION;
            case "CAREER":
                return CAREER;
            case "FAMILY":
                return FAMILY;
            case "LOVE":
                return LOVE;
            case "OTHER":
                return OTHER;
            default:
                return OTHER; // 默认返回OTHER
        }
    }
}