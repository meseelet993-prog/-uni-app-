-- 创建数据库（如未创建）
CREATE DATABASE IF NOT EXISTS mental_health_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mental_health_platform;

-- 聊天消息表结构
CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    receiver_id BIGINT NOT NULL COMMENT '接收者ID',
    content TEXT COMMENT '消息内容',
    type VARCHAR(20) DEFAULT 'text' COMMENT '消息类型',
    is_read TINYINT(1) DEFAULT 0 COMMENT '是否已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_sender_receiver (sender_id, receiver_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';
