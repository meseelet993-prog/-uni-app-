CREATE TABLE ticket_status_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    old_status VARCHAR(20) NOT NULL,
    new_status VARCHAR(20) NOT NULL,
    operator_id BIGINT NOT NULL,
    operator_role VARCHAR(20) NOT NULL,
    remark VARCHAR(500),
    created_at DATETIME NOT NULL
);

-- 添加外键约束
ALTER TABLE ticket_status_log 
ADD CONSTRAINT fk_ticket_status_log_ticket_id 
FOREIGN KEY (ticket_id) REFERENCES ticket(id);