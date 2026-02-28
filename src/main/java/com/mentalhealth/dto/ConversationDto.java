package com.mentalhealth.dto;

import com.mentalhealth.entity.ChatMessage;
import lombok.Data;

@Data
public class ConversationDto {
    private Long partnerId;
    private ChatMessage lastMessage;
}
