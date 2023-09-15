package com.tiktok.model.vo.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.entity.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;


@Data
@AllArgsConstructor
public class MessageResp {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    @JsonProperty("message_list")
    private List<MessageVo> messageList;
}
