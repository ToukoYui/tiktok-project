package com.tiktok.service_chat.controller;

import com.tiktok.model.vo.chat.ChatMessageResp;
import com.tiktok.service_chat.service.ChatService;
import com.tiktok.service_chat.service.chat_user.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @PostMapping("list")
    public ChatMessageResp msgList(@RequestBody ChatUser chatUser){
        return chatService.list(chatUser);
    }
}
