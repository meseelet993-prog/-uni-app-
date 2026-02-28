package com.mentalhealth.dto;

import com.mentalhealth.entity.TicketMessage;
import lombok.Data;
import java.util.List;

@Data
public class TicketMessageResponse {
    /**
     * 消息列表
     */
    private List<TicketMessage> messages;
    
    /**
     * 消息总数
     */
    private long total;
    
    public TicketMessageResponse(List<TicketMessage> messages, long total) {
        this.messages = messages;
        this.total = total;
    }
}