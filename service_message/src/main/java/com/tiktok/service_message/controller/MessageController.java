package com.tiktok.service_message.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.entity.message.LatestMsg;
import com.tiktok.model.entity.message.Message;
import com.tiktok.model.vo.message.MessageResp;
import com.tiktok.model.vo.message.MessageVo;
import com.tiktok.service_message.service.ChatgptService;
import com.tiktok.service_message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 查询聊天记录
     *
     * @param toUserId
     * @return
     */
    @GetMapping("/chat")
    public MessageResp getMessageList(@RequestParam("to_user_id") Long toUserId, @RequestParam("pre_msg_time") Long preMsgTime, @TokenAuthAnno Long userId) {
        log.info("前端传来的时间戳------------------>" + preMsgTime);
        LocalDateTime preTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(preMsgTime), ZoneId.systemDefault());
        if (preMsgTime.equals(0L)) {
            preTime = null;
        }
        // 获取本人的发过的消息
        List<Message> messageList = messageService.getMessageList(userId, toUserId, preTime);
        // 获取对方发过的消息
        List<Message> messageList2 = messageService.getMessageList(toUserId, userId, preTime);
        // 组合后按发送时间升序
        messageList.addAll(messageList2);
        // 由于前端需要时间戳，LocalDateTime只能转了
        List<MessageVo> messageVoList = messageList.stream().map(message -> {
            MessageVo messageVo = new MessageVo();
            messageVo.setContent(message.getContent());
            messageVo.setUserId(message.getUserId());
            messageVo.setToUserId(message.getToUserId());
            Long timestamp = message.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            messageVo.setCreateTime(timestamp);
            return messageVo;
        }).sorted(Comparator.comparing(MessageVo::getCreateTime)).collect(Collectors.toList());
        return new MessageResp("0", "获取聊天记录成功", messageVoList);
    }
    @Autowired
    private ChatgptService chatgptService;
    @PostMapping("/action")
    public MessageResp sendMessage(@RequestParam("to_user_id") Long toUserId, String content, @TokenAuthAnno Long userId) {
        try {
            Message message = new Message();
            message.setContent(content);
            message.setUserId(userId);
            message.setToUserId(toUserId);
            message.setCreateTime(LocalDateTime.now());
            messageService.sendMessage(message);
            if(toUserId == 7){
                //防止ai没有给予回答再次发送问题
                if(redisTemplate.hasKey(userId+"DontRepeatSend")){
                    return new MessageResp("444","请等待ai回答",null);
                }
                redisTemplate.opsForValue().set(userId+"DontRepeatSend","");
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
