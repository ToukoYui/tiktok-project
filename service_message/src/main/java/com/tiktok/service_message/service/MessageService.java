package com.tiktok.service_message.service;

import com.tiktok.model.entity.message.LatestMsg;
import com.tiktok.model.entity.message.Message;
import com.tiktok.service_message.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private RedisTemplate<String,Integer> redisTemplate;

    public List<Message> getMessageList(Long userId, Long toUserId, LocalDateTime preTime) {
        List<Message> messageList = messageMapper.getMessageList(userId, toUserId, preTime);
        String key = "message:" + userId + "_" + toUserId + ":";
        List<Message> resultList = new ArrayList<>();
        // 如果preTime为空，说明用户第一次打开聊天界面，直接返回
        if (preTime == null){
            redisTemplate.opsForValue().set(key,messageList.size());
            return messageList;
        }
        // 如果存在缓存。则比对是否有新消息
        if (redisTemplate.hasKey(key)){
            Integer msgCount = redisTemplate.opsForValue().get(key);
            if (messageList.size() > msgCount){ //说明有新消息
                // 更新最新的消息条数
                redisTemplate.opsForValue().set(key,messageList.size());
                // 拿最新的n条消息
//                resultList = messageList.subList(msgCount-1,messageList.size());  //前端有实时显示，这个不需要
            }
        }else{
            redisTemplate.opsForValue().set(key,messageList.size());
            return messageList;
        }
        return resultList;
    }

    public void sendMessage(Message message) {
        messageMapper.insertMessage(message);
    }

    public LatestMsg getLatestMessage(Long userId, Long friendId) {
        Message userMessage = messageMapper.getLatestMessage(userId);
        Message friendMessage = messageMapper.getLatestMessage(friendId);
        LatestMsg latestMsg = new LatestMsg();
        if (userMessage != null && friendMessage != null) {
            if (userMessage.getCreateTime().isAfter(friendMessage.getCreateTime())) {
                latestMsg.setMessage(userMessage.getContent());
                latestMsg.setMsgType(1);
            } else {
                latestMsg.setMessage(friendMessage.getContent());
                latestMsg.setMsgType(0);
            }
        } else {
            if (userMessage == null) {
                latestMsg.setMessage(friendMessage.getContent());
                latestMsg.setMsgType(0);
            } else {
                latestMsg.setMessage(userMessage.getContent());
                latestMsg.setMsgType(1);
            }
        }
        return latestMsg;
    }
}
