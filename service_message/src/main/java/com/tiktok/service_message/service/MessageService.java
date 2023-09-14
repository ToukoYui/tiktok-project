package com.tiktok.service_message.service;

import com.tiktok.model.entity.message.Message;
import com.tiktok.service_message.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> getMessageList(Long userId,Long toUserId){
        return messageMapper.getMessageList(userId, toUserId);
    }

    public void sendMessage(Message message){
        messageMapper.insertMessage(message);
    }
}
