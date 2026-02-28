package com.mentalhealth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== 开始初始化数据库表结构 ===");

        // 分别检查每个表是否存在
        boolean ticketTableExists = checkTableExists("ticket");
        boolean logTableExists = checkTableExists("ticket_status_log");
        boolean messageTableExists = checkTableExists("ticket_message");
        boolean chatMessagesTableExists = checkTableExists("chat_messages");

        log.info("ticket表存在: {}", ticketTableExists);
        log.info("ticket_status_log表存在: {}", logTableExists);
        log.info("ticket_message表存在: {}", messageTableExists);
        log.info("chat_messages表存在: {}", chatMessagesTableExists);

        if (!ticketTableExists) {
            log.info("ticket表不存在，开始创建...");
            createTicketTable();
        } else {
            log.info("ticket表已存在，跳过创建");
        }

        if (!logTableExists) {
            log.info("ticket_status_log表不存在，开始创建...");
            createTicketStatusLogTable();
        } else {
            log.info("ticket_status_log表已存在，跳过创建");
        }
        
        if (!messageTableExists) {
            log.info("ticket_message表不存在，开始创建...");
            createTicketMessageTable();
        } else {
            log.info("ticket_message表已存在，跳过创建");
        }
        
        if (!chatMessagesTableExists) {
            log.info("chat_messages表不存在，开始创建...");
            createChatMessagesTable();
        } else {
            log.info("chat_messages表已存在，跳过创建");
        }

        log.info("=== 数据库表结构初始化完成 ===");
    }

    private boolean checkTableExists(String tableName) {
        String sql = "SHOW TABLES LIKE ?";
        List<String> tables = jdbcTemplate.queryForList(sql, String.class, tableName);
        return !tables.isEmpty();
    }

    private void createTicketTable() {
        String sql = "CREATE TABLE `ticket` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `ticket_no` varchar(32) NOT NULL COMMENT '工单编号'," +
                "  `title` varchar(200) NOT NULL COMMENT '标题'," +
                "  `content` text COMMENT '内容'," +
                "  `category` varchar(20) NOT NULL DEFAULT 'GENERAL' COMMENT '分类'," +
                "  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '状态'," +
                "  `priority` varchar(10) DEFAULT 'medium' COMMENT '优先级'," +
                "  `student_id` bigint NOT NULL COMMENT '学生ID'," +
                "  `counselor_id` bigint COMMENT '咨询师ID'," +
                "  `counselor_name` varchar(50) COMMENT '咨询师姓名'," +
                "  `is_anonymous` tinyint(1) DEFAULT 0 COMMENT '是否匿名'," +
                "  `attachments` text COMMENT '附件列表'," +
                "  `is_deleted` int DEFAULT 0 COMMENT '是否删除'," +
                "  `create_time` datetime NOT NULL COMMENT '创建时间'," +
                "  `update_time` datetime NOT NULL COMMENT '更新时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_ticket_no` (`ticket_no`)," +
                "  KEY `idx_student_id` (`student_id`)," +
                "  KEY `idx_counselor_id` (`counselor_id`)," +
                "  KEY `idx_status` (`status`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';";
        jdbcTemplate.execute(sql);
        log.info("ticket表创建成功");
    }

    private void createTicketStatusLogTable() {
        String sql = "CREATE TABLE ticket_status_log (" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "    ticket_id BIGINT NOT NULL," +
                "    old_status VARCHAR(20) NOT NULL," +
                "    new_status VARCHAR(20) NOT NULL," +
                "    operator_id BIGINT NOT NULL," +
                "    operator_role VARCHAR(20) NOT NULL," +
                "    remark VARCHAR(500)," +
                "    created_at DATETIME NOT NULL" +
                ");";
        jdbcTemplate.execute(sql);
        log.info("ticket_status_log表创建成功");

        // 添加外键约束
        String fkSql = "ALTER TABLE ticket_status_log " +
                "ADD CONSTRAINT fk_ticket_status_log_ticket_id " +
                "FOREIGN KEY (ticket_id) REFERENCES ticket(id);";
        jdbcTemplate.execute(fkSql);
        log.info("外键约束添加成功");
    }
    
    private void createTicketMessageTable() {
        String sql = "CREATE TABLE `ticket_message` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `ticket_id` bigint NOT NULL COMMENT '关联工单ID'," +
                "  `sender_id` bigint NOT NULL COMMENT '发送者ID'," +
                "  `sender_role` varchar(20) NOT NULL COMMENT '发送者角色'," +
                "  `content` text NOT NULL COMMENT '消息内容'," +
                "  `message_type` varchar(20) DEFAULT 'text' COMMENT '消息类型'," +
                "  `is_read` tinyint(1) DEFAULT 0 COMMENT '是否已读'," +
                "  `created_at` datetime NOT NULL COMMENT '创建时间'," +
                "  `updated_at` datetime NOT NULL COMMENT '更新时间'," +
                "  `is_deleted` int DEFAULT 0 COMMENT '是否删除'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_ticket_id` (`ticket_id`)," +
                "  KEY `idx_created_at` (`created_at`)," +
                "  KEY `idx_is_read` (`is_read`)," +
                "  CONSTRAINT `fk_ticket_message_ticket_id` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单消息表';";
        jdbcTemplate.execute(sql);
        log.info("ticket_message表创建成功");
    }
    
    private void createChatMessagesTable() {
        String sql = "CREATE TABLE `chat_messages` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  `sender_id` bigint NOT NULL COMMENT '发送者ID'," +
                "  `receiver_id` bigint NOT NULL COMMENT '接收者ID'," +
                "  `content` text COMMENT '消息内容'," +
                "  `type` varchar(20) DEFAULT 'text' COMMENT '消息类型'," +
                "  `is_read` tinyint(1) DEFAULT 0 COMMENT '是否已读'," +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_sender_receiver` (`sender_id`,`receiver_id`)," +
                "  KEY `idx_create_time` (`create_time`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';";
        jdbcTemplate.execute(sql);
        log.info("chat_messages表创建成功");
    }
}
