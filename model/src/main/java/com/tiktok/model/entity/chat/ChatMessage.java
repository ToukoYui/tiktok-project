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

    //关系表id
    private Long linkId;

    //发送者
    private String fromUser;

    //接收者
    private String toUser;

    //内容
    private String content;

    //发送时间
    private Date sendTime;

    //消息类型  0--普通文本（默认）
    private int type = MESSAGE_TYPE_TEXT;

    //是否为最后一条
    private Boolean isLatest;

    public ChatMessage() {
    }

    public ChatMessage(Long linkId, String fromUser, String toUser, String content, Date sendTime, Boolean isLatest) {
        this.linkId = linkId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.sendTime = sendTime;
        this.isLatest = isLatest;
    }
}

