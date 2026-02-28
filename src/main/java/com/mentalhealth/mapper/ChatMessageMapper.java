package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 聊天消息Mapper接口
 */
@Mapper
@Repository
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}
