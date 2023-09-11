package com.tiktok.model.entity.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatUserLink {
    private Long id;
    private String fromUser;
    private String toUser;
    private LocalDateTime creatTime;
}
