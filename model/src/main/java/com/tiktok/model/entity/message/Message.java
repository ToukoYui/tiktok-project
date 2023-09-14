package com.tiktok.model.entity.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Long id;

    @JsonProperty("from_user_id")
    private Long userId;

    @JsonProperty("to_user_id")
    private Long toUserId;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonProperty("create_time")
    private LocalDateTime createTime;
}
