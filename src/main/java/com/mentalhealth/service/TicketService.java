package com.mentalhealth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.constant.TicketStatusConstants;
import com.mentalhealth.constant.UserRoleConstants;
import com.mentalhealth.dto.*;
import com.mentalhealth.entity.*;
import com.mentalhealth.mapper.TicketMapper;
import com.mentalhealth.mapper.TicketMessageMapper;
import com.mentalhealth.mapper.TicketStatusLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TicketService extends ServiceImpl<TicketMapper, Ticket> {
    
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);
    
    @Autowired
    private TicketStatusLogMapper ticketStatusLogMapper;
    
    @Autowired
    private TicketMessageMapper ticketMessageMapper;

    /**
     * 创建工单
     * @param request 创建工单请求
     * @return 创建的工单
     */
    @Transactional
    public Ticket createTicket(CreateTicketRequest request) {
        log.info("=== 开始执行创建工单业务逻辑 ===");
        log.debug("传入的请求参数: {}", request);
        
        Ticket ticket = new Ticket();
        log.info("[流程1/12] 初始化Ticket对象");
        
        String ticketNo = generateTicketNo();
        ticket.setTicketNo(ticketNo);
        log.info("[流程2/12] 生成工单编号: {}", ticketNo);
        
        ticket.setStudentId(request.getStudentId());
        log.info("[流程3/12] 设置学生ID: {}", request.getStudentId());
        
        // 修复：先检查title字段是否存在再设置
        if (request.getTitle() != null) {
            ticket.setTitle(request.getTitle());
            log.info("[流程4/12] 设置工单标题: {}", request.getTitle());
        } else {
            ticket.setTitle("未命名工单");
            log.info("[流程4/12] 工单标题为空，使用默认标题");
        }
        
        // 修复：先检查content字段是否存在再设置
        if (request.getContent() != null) {
            ticket.setContent(request.getContent());
            log.info("[流程5/12] 设置工单内容: {}", request.getContent());
        } else {
            ticket.setContent("无详细内容");
            log.info("[流程5/12] 工单内容为空，使用默认内容");
        }
        
        // 修复：先检查category字段是否存在再设置
        if (request.getCategory() != null) {
            // 使用枚举类型设置分类
            TicketCategory category = TicketCategory.fromString(request.getCategory());
            ticket.setCategoryEnum(category);
            log.info("[流程6/12] 设置工单分类: {}", category.name());
        } else {
            ticket.setCategoryEnum(TicketCategory.OTHER);
            log.info("[流程6/12] 工单分类为空，使用默认分类OTHER");
        }
        
        // 修复：先检查priority字段是否存在再设置
        if (request.getPriority() != null) {
            // 使用枚举类型设置优先级
            TicketPriority priority = TicketPriority.fromString(request.getPriority());
            ticket.setPriorityEnum(priority);
            log.info("[流程7/12] 设置工单优先级: {}", priority.name());
        } else {
            ticket.setPriorityEnum(TicketPriority.MEDIUM);
            log.info("[流程7/12] 工单优先级为空，使用默认优先级MEDIUM");
        }
        
        Boolean isAnonymous = request.getIsAnonymous() != null ? request.getIsAnonymous() : false;
        ticket.setIsAnonymous(isAnonymous);
        log.info("[流程8/12] 设置是否匿名: {}", isAnonymous);
        
        // 处理附件列表，确保不会因为空数组导致问题
        if (request.getAttachments() != null && request.getAttachments().length > 0) {
            String attachments = String.join(",", request.getAttachments());
            ticket.setAttachments(attachments);
            log.info("[流程9/12] 设置附件列表: {}", attachments);
        } else {
            ticket.setAttachments(null);
            log.info("[流程9/12] 未设置附件");
        }
        
        ticket.setStatusEnum(TicketStatus.PENDING);
        log.info("[流程10/12] 设置初始状态: {}", TicketStatus.PENDING.name());
        
        LocalDateTime now = LocalDateTime.now();
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        log.info("[流程11/12] 设置创建时间和更新时间: {}", now);
        
        log.info("[流程12/12] 完成工单创建");
        
        log.info("准备保存工单到数据库");
        this.save(ticket);
        log.info("工单保存成功，工单ID: {}", ticket.getId());
        
        log.info("=== 工单创建业务逻辑执行完毕 ===");
        return ticket;
    }
    
    /**
     * 分页查询工单
     * @param page 页码
     * @param pageSize 每页大小
     * @param status 状态筛选
     * @param studentId 学生ID筛选
     * @param counselorId 咨询师ID筛选
     * @return 工单分页列表
     */
    public IPage<Ticket> getTicketList(Integer page, Integer pageSize, String status, Long studentId, Long counselorId) {
        Page<Ticket> pageInfo = new Page<>(page != null ? page : 1, pageSize != null ? pageSize : 10);
        
        QueryWrapper<Ticket> queryWrapper = new QueryWrapper<>();
        
        // 状态筛选
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        // 学生ID筛选
        if (studentId != null) {
            queryWrapper.eq("student_id", studentId);
        }
        
        // 咨询师ID筛选
        if (counselorId != null) {
            queryWrapper.eq("counselor_id", counselorId);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return this.page(pageInfo, queryWrapper);
    }
    
    /**
     * 获取工单详情
     * @param id 工单ID
     * @return 工单详情
     */
    public TicketDetailResponse getTicketDetail(Long id) {
        Ticket ticket = this.getById(id);
        if (ticket == null) {
            return null;
        }
        
        TicketDetailResponse response = new TicketDetailResponse();
        response.setTicket(ticket);
        
        // 查询状态变更历史
        QueryWrapper<TicketStatusLog> logQueryWrapper = new QueryWrapper<>();
        logQueryWrapper.eq("ticket_id", id);
        logQueryWrapper.orderByAsc("created_at");
        List<TicketStatusLog> statusLogs = ticketStatusLogMapper.selectList(logQueryWrapper);
        response.setStatusLogs(statusLogs);
        
        return response;
    }
    
    /**
     * 更新工单状态
     * @param id 工单ID
     * @param request 更新状态请求
     * @return 更新后的工单
     */
    @Transactional
    public Ticket updateTicketStatus(Long id, UpdateTicketStatusRequest request) {
        Ticket ticket = this.getById(id);
        if (ticket == null) {
            throw new RuntimeException("工单不存在");
        }
        
        String oldStatus = ticket.getStatus();
        String newStatus = request.getStatus();
        
        // 更新工单状态
        ticket.setStatus(newStatus);
        ticket.setUpdateTime(LocalDateTime.now());
        
        // 如果是进行中状态且操作人是咨询师，则设置咨询师ID
        if (TicketStatus.IN_PROGRESS.name().toLowerCase().equals(newStatus) && 
            request.getOperatorId() != null && 
            UserRoleConstants.CONSULTANT.equals(request.getOperatorRole())) {
            ticket.setCounselorId(request.getOperatorId());
        }
        
        this.updateById(ticket);
        
        // 记录状态变更日志
        TicketStatusLog statusLog = new TicketStatusLog();
        statusLog.setTicketId(id);
        statusLog.setOldStatus(oldStatus);
        statusLog.setNewStatus(newStatus);
        statusLog.setOperatorId(request.getOperatorId());
        statusLog.setOperatorRole(request.getOperatorRole());
        statusLog.setRemark(request.getRemark());
        statusLog.setCreatedAt(LocalDateTime.now());
        ticketStatusLogMapper.insert(statusLog);
        
        return ticket;
    }
    
    /**
     * 软删除工单
     * @param id 工单ID
     */
    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = this.getById(id);
        if (ticket == null) {
            throw new RuntimeException("工单不存在");
        }
        
        // 硬删除
        this.removeById(id);
    }
    
    /**
     * 发送工单消息
     * @param ticketId 工单ID
     * @param request 发送消息请求
     * @return 创建的消息
     */
    @Transactional
    public TicketMessage sendTicketMessage(Long ticketId, CreateTicketMessageRequest request) {
        log.info("=== 开始发送工单消息，工单ID: {} ===", ticketId);
        
        // 验证工单是否存在
        Ticket ticket = this.getById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("工单不存在");
        }
        
        TicketMessage message = new TicketMessage();
        message.setTicketId(ticketId);
        message.setSenderId(request.getSenderId());
        message.setSenderRole(request.getSenderRole());
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType() != null ? request.getMessageType() : "text");
        message.setIsRead(false);
        
        LocalDateTime now = LocalDateTime.now();
        message.setCreatedAt(now);
        message.setUpdatedAt(now);

        
        ticketMessageMapper.insert(message);
        
        log.info("工单消息发送成功，消息ID: {}", message.getId());
        return message;
    }
    
    /**
     * 获取工单消息列表
     * @param ticketId 工单ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 消息分页列表
     */
    public IPage<TicketMessage> getTicketMessages(Long ticketId, Integer page, Integer pageSize) {
        log.info("=== 开始获取工单消息列表，工单ID: {} ===", ticketId);
        
        Page<TicketMessage> pageInfo = new Page<>(page != null ? page : 1, pageSize != null ? pageSize : 10);
        
        QueryWrapper<TicketMessage> queryWrapper = new QueryWrapper<>();
        // 查询指定工单的消息
        queryWrapper.eq("ticket_id", ticketId);
        // 按创建时间升序排列
        queryWrapper.orderByAsc("created_at");
        
        IPage<TicketMessage> result = ticketMessageMapper.selectPage(pageInfo, queryWrapper);
        log.info("获取工单消息列表成功，共 {} 条消息", result.getTotal());
        
        return result;
    }
    
    /**
     * 标记消息为已读
     * @param messageId 消息ID
     */
    @Transactional
    public void markMessageAsRead(Long messageId) {
        log.info("=== 开始标记消息为已读，消息ID: {} ===", messageId);
        
        TicketMessage message = ticketMessageMapper.selectById(messageId);
        if (message != null) {
            message.setIsRead(true);
            message.setUpdatedAt(LocalDateTime.now());
            ticketMessageMapper.updateById(message);
            log.info("消息已标记为已读");
        }
    }
    
    /**
     * 获取工单状态记录
     * @param ticketId 工单ID
     * @return 状态记录列表
     */
    public List<TicketStatusLog> getTicketStatusLogs(Long ticketId) {
        log.info("=== 开始获取工单状态记录，工单ID: {} ===", ticketId);
        
        QueryWrapper<TicketStatusLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ticket_id", ticketId);
        queryWrapper.orderByAsc("created_at");
        
        List<TicketStatusLog> statusLogs = ticketStatusLogMapper.selectList(queryWrapper);
        log.info("获取工单状态记录成功，共 {} 条记录", statusLogs.size());
        
        return statusLogs;
    }
    
    /**
     * 生成工单编号（格式：TK+年月日+6位随机数）
     * @return 工单编号
     */
    private String generateTicketNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = LocalDateTime.now().format(formatter);
        SecureRandom random = new SecureRandom();
        int randomNum = 100000 + random.nextInt(900000); // 6位随机数
        return "TK" + dateStr + randomNum;
    }
    
    /**
     * 验证工单状态是否有效
     * @param status 状态值
     * @return 是否有效
     */
    private boolean isValidTicketStatus(String status) {
        if (status == null) {
            return false;
        }
        
        return "pending".equalsIgnoreCase(status) ||
               "in_progress".equalsIgnoreCase(status) ||
               "completed".equalsIgnoreCase(status) ||
               "closed".equalsIgnoreCase(status);
    }
}