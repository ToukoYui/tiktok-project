package com.tiktok.service_chat.mapper;

import com.tiktok.model.entity.chat.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ChatMessageMapper {
    void save(ChatMessage chatMsg);
    List<ChatMessage> selectSend(String fromUser,String toUser);
}
