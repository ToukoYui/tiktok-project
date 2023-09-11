package com.tiktok.service_chat.mapper;

import com.tiktok.model.entity.chat.ChatMessage;

import java.util.List;

public interface ChatMessageMapper {
    void save(ChatMessage chatMsg);
    List<ChatMessage> selectSend(Long linkId);
}
