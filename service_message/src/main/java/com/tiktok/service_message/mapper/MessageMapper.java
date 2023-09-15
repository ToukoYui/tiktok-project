package com.tiktok.service_message.mapper;

import com.tiktok.model.entity.message.Message;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface MessageMapper {
    List<Message> getMessageList(Long userId, Long toUserId, LocalDateTime preTime);

    void insertMessage(Message message);

    Message getLatestMessage(Long userId);

}
