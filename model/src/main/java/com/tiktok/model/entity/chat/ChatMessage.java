package com.tiktok.model.entity.chat;

import lombok.Data;

import java.util.Date;
@Data
public class ChatMessage {

    //文本
    public static final int MESSAGE_TYPE_TEXT = 0;

    //图片
    public static final int MESSAGE_TYPE_IMAGE = 1;


    //信息id（自增）
    private Long id;

    //发送者
    private String fromUser;

    //接收者
    private String toUser;

    //内容
    private String content;

    //发送时间
    private Date sendTime;

    public ChatMessage() {
    }
    public ChatMessage(Long linkId, String fromUser, String toUser, String content, Date sendTime, Boolean isLatest) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.sendTime = sendTime;
    }
}

