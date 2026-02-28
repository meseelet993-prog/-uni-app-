DROP TABLE IF EXISTS `ticket`;

CREATE TABLE `ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ticket_no` varchar(32) NOT NULL COMMENT '工单编号',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `category` varchar(20) NOT NULL DEFAULT 'GENERAL' COMMENT '分类',
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '状态',
  `priority` varchar(10) DEFAULT 'medium' COMMENT '优先级',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `counselor_id` bigint COMMENT '咨询师ID',
  `counselor_name` varchar(50) COMMENT '咨询师姓名',
  `is_anonymous` tinyint(1) DEFAULT 0 COMMENT '是否匿名',
  `attachments` text COMMENT '附件列表',
  `is_deleted` int DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_no` (`ticket_no`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_counselor_id` (`counselor_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

-- 工单消息表
DROP TABLE IF EXISTS `ticket_message`;

CREATE TABLE `ticket_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ticket_id` bigint NOT NULL COMMENT '关联工单ID',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `sender_role` varchar(20) NOT NULL COMMENT '发送者角色',
  `content` text NOT NULL COMMENT '消息内容',
  `message_type` varchar(20) DEFAULT 'text' COMMENT '消息类型',
  `is_read` tinyint(1) DEFAULT 0 COMMENT '是否已读',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` int DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单消息表';