package com.tiktok.service_chat.service;

import com.tiktok.model.entity.chat.ChatMessage;
import com.tiktok.service_chat.mapper.ChatMessageMapper;
import com.tiktok.service_chat.service.chat_user.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    public synchronized void saveMessage(ChatMessage chatMsg) {
        chatMessageMapper.save(chatMsg);
    }
    public List<ChatMessage> list(ChatUser chatUser) {
        String sendOrRece = chatUser.getFistUsername();
        String receOrSend = chatUser.getSecondUsername();
        List<ChatMessage> messageListFirst = chatMessageMapper.selectSend(sendOrRece,receOrSend);
        List<ChatMessage> messageListSecond = chatMessageMapper.selectSend(receOrSend,sendOrRece);
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
            }else if(first == messageListFirst.size() && second < messageListSecond.size()){
                messageList.add(messageListSecond.get(second));
                second++;
            }else {
                long sendTimeFirst = messageListFirst.get(first).getSendTime().getTime();
                long sendTimeSecond = messageListSecond.get(second).getSendTime().getTime();
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
