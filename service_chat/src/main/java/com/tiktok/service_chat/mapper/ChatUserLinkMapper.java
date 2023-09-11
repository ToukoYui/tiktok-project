package com.tiktok.service_chat.mapper;

import com.tiktok.model.entity.chat.ChatUserLink;

public interface ChatUserLinkMapper {
    Long getOneRecord(String fromUser,String toUser);

    void save(ChatUserLink chatUserLink);
}
