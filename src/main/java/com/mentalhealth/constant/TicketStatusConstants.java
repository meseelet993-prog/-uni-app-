package com.mentalhealth.constant;

/**
 * 工单状态常量类 - 已废弃，请直接使用TicketStatus枚举
 * @deprecated 使用com.mentalhealth.entity.TicketStatus枚举替代
 */
@Deprecated
public class TicketStatusConstants {
    /**
     * 待受理
     */
    public static final String PENDING = "pending";
    
    /**
     * 咨询中
     */
    public static final String IN_PROGRESS = "in_progress";
    
    /**
     * 已完成
     */
    public static final String COMPLETED = "completed";
    
    /**
     * 已关闭
     */
    public static final String CLOSED = "closed";
    
    /**
     * 所有有效状态
     */
    public static final String[] ALL_STATUS = {PENDING, IN_PROGRESS, COMPLETED, CLOSED};
}