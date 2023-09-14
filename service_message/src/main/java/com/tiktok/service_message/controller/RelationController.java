package com.tiktok.service_message.controller;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.entity.message.Message;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.message.MessageResp;
import com.tiktok.service_message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/message")
public class RelationController {
    @Autowired
    private MessageService messageService;

    /**
     * 查询聊天记录
     *
     * @param toUserId
     * @param tokenAuthSuccess
     * @return
     */
    @GetMapping("/chat")
    public MessageResp getMessageList(@RequestParam("to_user_id") Long toUserId, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new MessageResp("401", "请先登录哦~", null);
        }
        Long userId = Long.valueOf(tokenAuthSuccess.getUserId());
        List<Message> messageList = messageService.getMessageList(userId, toUserId);
        return new MessageResp("0", "获取聊天记录成功", messageList);

    }

    @PostMapping("/action")
    public MessageResp sendMessage(@RequestParam("to_user_id") Long toUserId,String content,@TokenAuthAnno TokenAuthSuccess tokenAuthSuccess){
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new MessageResp("401", "请先登录哦~", null);
        }
        try{
            Long userId = Long.valueOf(tokenAuthSuccess.getUserId());
            Message message = new Message();
            message.setContent(content);
            message.setUserId(userId);
            message.setToUserId(toUserId);
            message.setCreateTime(LocalDateTime.now());
            messageService.sendMessage(message);
        }catch (Exception e){
            log.error(e.getMessage());
            return new MessageResp("500","发送失败",null);
        }
        return new MessageResp("0","发送成功",null);
    }

//    @GetMapping("/inner")
//    public String

}
