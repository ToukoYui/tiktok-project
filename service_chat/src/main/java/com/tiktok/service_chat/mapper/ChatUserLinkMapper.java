package com.tiktok.service_chat.mapper;

import com.tiktok.model.entity.chat.ChatUserLink;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatUserLinkMapper {
    Long getOneRecord(String fromUser,String toUser);

    void save(ChatUserLink chatUserLink);
}
