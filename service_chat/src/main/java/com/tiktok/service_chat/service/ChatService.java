package com.tiktok.service_chat.service;

import com.tiktok.model.entity.chat.ChatMessage;
import com.tiktok.model.entity.chat.ChatUserLink;
import com.tiktok.model.vo.chat.ChatMessageResp;
import com.tiktok.service_chat.mapper.ChatMessageMapper;
import com.tiktok.service_chat.mapper.ChatUserLinkMapper;
import com.tiktok.service_chat.service.chat_user.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatUserLinkMapper chatUserLinkMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    public Long selectAssociation(String username, String toUser) {
        Long linkId = chatUserLinkMapper.getOneRecord(username,toUser);
        if (linkId != null){
            return linkId;
        }
        ChatUserLink chatUserLink = new ChatUserLink();
        chatUserLink.setFromUser(username);
        chatUserLink.setToUser(toUser);
        chatUserLinkMapper.save(chatUserLink);
        return chatUserLinkMapper.getOneRecord(username,toUser);
    }
    public void saveMessage(ChatMessage chatMsg) {
        chatMessageMapper.save(chatMsg);
    }
    public List<ChatMessage> list(ChatUser chatUser) {
        String sendOrRece = chatUser.getFistUsername();
        String receOrSend = chatUser.getSecondUsername();
        Long firstLindId = chatUserLinkMapper.getOneRecord(sendOrRece, receOrSend);
        Long secondLindId = chatUserLinkMapper.getOneRecord(receOrSend, sendOrRece);
        List<ChatMessage> messageListFirst = new ArrayList<>();
        if(firstLindId != null){
            messageListFirst = chatMessageMapper.selectSend(firstLindId);
        }
        List<ChatMessage> messageListSecond = new ArrayList<>();
        if(secondLindId != null){
            messageListSecond = chatMessageMapper.selectSend(secondLindId);
        }
        List<ChatMessage> messageList = new ArrayList<>();
        int first = 0;
        int second = 0;
        //根据时间顺序对消息展示进行处理
        while(true){
            if(first == messageListFirst.size() && second == messageListSecond.size()){
                break;
            }else if(first < messageListFirst.size() && messageListSecond.size() == second){
               messageList.add(messageListFirst.get(first));
               first++;
            }else if(first == messageListFirst.size() && messageListSecond.size() < second){
                messageList.add(messageListSecond.get(second));
                second++;
            }else {
                long sendTimeFirst = messageListFirst.get(first).getSendTime().getTime();
                long sendTimeSecond = messageListFirst.get(first).getSendTime().getTime();
                if(sendTimeFirst < sendTimeSecond){
                    messageList.add(messageListFirst.get(first));
                    first++;
                }else{
                    messageList.add(messageListSecond.get(second));
                    second++;
                }
            }
        }
        return messageList;
    }
}
