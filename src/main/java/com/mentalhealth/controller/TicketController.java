package com.mentalhealth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mentalhealth.dto.ApiResponse;
import com.mentalhealth.dto.CreateTicketMessageRequest;
import com.mentalhealth.dto.CreateTicketRequest;
import com.mentalhealth.dto.TicketDetailResponse;
import com.mentalhealth.dto.TicketListRequest;
import com.mentalhealth.dto.UpdateTicketStatusRequest;
import com.mentalhealth.entity.Ticket;
import com.mentalhealth.entity.TicketMessage;
import com.mentalhealth.entity.TicketStatusLog;
import com.mentalhealth.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tickets")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    /**
     * 创建工单接口
     * @param request 创建工单请求
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody CreateTicketRequest request) {
        try {
            log.info("=== 开始处理创建工单请求 ===");
            log.info("接收到的请求参数: {}", request);
            
            // 验证必要字段
            if (request.getStudentId() == null) {
                log.warn("[验证阶段] 创建工单请求缺少必要字段 studentId");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "缺少必要字段: studentId"));
            }
            
            log.info("[调用服务前] 准备调用TicketService.createTicket方法");
            Ticket ticket = ticketService.createTicket(request);
            log.info("[调用服务后] 工单创建成功，工单ID: {}", ticket.getId());
            
            log.info("[响应阶段] 返回创建成功的响应");
            return ResponseEntity.ok(ApiResponse.success(ticket));
        } catch (Exception e) {
            log.error("[异常处理] 创建工单失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "创建工单失败: " + e.getMessage()));
        } finally {
            log.info("=== 创建工单请求处理结束 ===");
        }
    }
    
    /**
     * 获取工单列表接口
     * @param page 页码
     * @param pageSize 每页大小
     * @param status 状态筛选
     * @param studentId 学生ID筛选
     * @param counselorId 咨询师ID筛选
     * @return 工单列表
     */
    @GetMapping("/list")
    public ResponseEntity<?> getTickets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long counselorId) {
        try {
            log.info("收到获取工单列表请求: page={}, pageSize={}, status={}, studentId={}, counselorId={}", page, pageSize, status, studentId, counselorId);
            IPage<Ticket> tickets = ticketService.getTicketList(page, pageSize, status, studentId, counselorId);
            return ResponseEntity.ok(ApiResponse.success(tickets));
        } catch (Exception e) {
            log.error("获取工单列表失败", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "查询工单列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 工单详情接口
     * @param id 工单ID
     * @return 工单详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketDetail(@PathVariable Long id) {
        try {
            log.info("收到获取工单详情请求: id={}", id);
            TicketDetailResponse response = ticketService.getTicketDetail(id);
            if (response != null) {
                return ResponseEntity.ok(ApiResponse.success(response));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取工单详情失败: id={}", id, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "获取工单详情失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新工单状态接口
     * @param id 工单ID
     * @param request 更新状态请求
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTicketStatus(
            @PathVariable Long id, 
            @RequestBody UpdateTicketStatusRequest request) {
        try {
            log.info("收到更新工单状态请求: id={}, request={}", id, request);
            Ticket ticket = ticketService.updateTicketStatus(id, request);
            return ResponseEntity.ok(ApiResponse.success(ticket));
        } catch (Exception e) {
            log.error("更新工单状态失败: id={}", id, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "更新工单状态失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除工单接口
     * @param id 工单ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        try {
            log.info("收到删除工单请求: id={}", id);
            ticketService.deleteTicket(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("删除工单失败: id={}", id, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "删除工单失败: " + e.getMessage()));
        }
    }
    
    /**
     * 发送工单消息
     * @param id 工单ID
     * @param request 发送消息请求
     * @return 消息发送结果
     */
    @PostMapping("/{id}/messages")
    public ResponseEntity<?> sendTicketMessage(@PathVariable Long id, @RequestBody CreateTicketMessageRequest request) {
        try {
            log.info("收到发送工单消息请求: 工单ID={}, 发送者ID={}, 发送者角色={}", id, request.getSenderId(), request.getSenderRole());
            TicketMessage message = ticketService.sendTicketMessage(id, request);
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            log.error("发送工单消息失败: 工单ID={}", id, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "发送工单消息失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取工单消息列表
     * @param id 工单ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 消息列表
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getTicketMessages(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            log.info("收到获取工单消息列表请求: 工单ID={}, page={}, pageSize={}", id, page, pageSize);
            IPage<TicketMessage> messages = ticketService.getTicketMessages(id, page, pageSize);
            return ResponseEntity.ok(ApiResponse.success(messages));
        } catch (Exception e) {
            log.error("获取工单消息列表失败: 工单ID={}", id, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "获取工单消息列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取工单状态记录
     * @param id 工单ID
     * @return 状态记录列表
     */
    @GetMapping("/{id}/status-logs")
    public ResponseEntity<?> getTicketStatusLogs(@PathVariable Long id) {
        try {
            log.info("收到获取工单状态记录请求: 工单ID={}", id);
            List<TicketStatusLog> statusLogs = ticketService.getTicketStatusLogs(id);
            return ResponseEntity.ok(ApiResponse.success(statusLogs));
        } catch (Exception e) {
            log.error("获取工单状态记录失败: 工单ID={}", id, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "获取工单状态记录失败: " + e.getMessage()));
        }
    }
}
