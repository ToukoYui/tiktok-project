package com.tiktok.model.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVo {
    private Long id;

    @JsonProperty("from_user_id")
    private Long userId;

    @JsonProperty("to_user_id")
    private Long toUserId;

    private String content;

    @JsonProperty("create_time")
    private Long createTime;
}
