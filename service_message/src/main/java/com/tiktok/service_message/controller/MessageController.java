package com.tiktok.service_message.controller;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.entity.message.LatestMsg;
import com.tiktok.model.entity.message.Message;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.message.MessageResp;
import com.tiktok.model.vo.message.MessageVo;
import com.tiktok.service_message.service.ChatgptService;
import com.tiktok.service_message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {
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
    public MessageResp getMessageList(@RequestParam("to_user_id") Long toUserId, @RequestParam("pre_msg_time") Long preMsgTime, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new MessageResp("401", "请先登录哦~", null);
        }
        log.info("前端传来的时间戳------------------>" + preMsgTime);
        LocalDateTime preTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(preMsgTime), ZoneId.systemDefault());
        Long userId = Long.valueOf(tokenAuthSuccess.getUserId());
        if (preMsgTime.equals(0L)) {
            preTime = null;
        }
        List<Message> messageList = messageService.getMessageList(userId, toUserId, preTime);
        // 由于前端需要时间戳，LocalDateTime只能转了
        List<MessageVo> messageVoList = messageList.stream().map(message -> {
            MessageVo messageVo = new MessageVo();
            messageVo.setContent(message.getContent());
            messageVo.setUserId(message.getUserId());
            messageVo.setToUserId(message.getToUserId());
            Long timestamp = message.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            messageVo.setCreateTime(timestamp);
            return messageVo;
        }).collect(Collectors.toList());
        return new MessageResp("0", "获取聊天记录成功", messageVoList);

    }
    @Autowired
    private ChatgptService chatgptService;
    @PostMapping("/action")
    public MessageResp sendMessage(@RequestParam("to_user_id") Long toUserId, String content, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new MessageResp("401", "请先登录哦~", null);
        }
        try {
            Long userId = Long.valueOf(tokenAuthSuccess.getUserId());
            Message message = new Message();
            message.setContent(content);
            message.setUserId(userId);
            message.setToUserId(toUserId);
            message.setCreateTime(LocalDateTime.now());
            messageService.sendMessage(message);
            if(toUserId == 7){

                chatgptService.send(message.getUserId(),message.getContent());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return new MessageResp("500", "发送失败", null);
        }
        return new MessageResp("0", "发送成功", null);
    }

    /**
     * 获取最新的一条消息
     *
     * @return
     */
    @GetMapping("/inner/latest")
    public LatestMsg getLatestMessage(@RequestParam("userId") Long userId, @RequestParam("friendId") Long friendId) {
        LatestMsg latestMsg = messageService.getLatestMessage(userId, friendId);
        if (latestMsg == null) {
            return null;
        }
        return latestMsg;
    }
}
