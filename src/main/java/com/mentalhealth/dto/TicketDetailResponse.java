package com.mentalhealth.dto;

import com.mentalhealth.entity.Ticket;
import lombok.Data;
import java.util.List;
import com.mentalhealth.entity.TicketStatusLog;

@Data
public class TicketDetailResponse {
    /**
     * 工单信息
     */
    private Ticket ticket;
    
    /**
     * 状态变更历史
     */
    private List<TicketStatusLog> statusLogs;
}