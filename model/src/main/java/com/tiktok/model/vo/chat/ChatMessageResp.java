package com.tiktok.model.vo.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.entity.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ChatMessageResp {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    @JsonProperty("message_list")
    private List<ChatMessage> messageList;
}
