package com.tiktok.model.entity.video;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Video {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("play_url")
    private String playUrl;

    @JsonProperty("cover_url")
    private String coverUrl;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("created_time")
    private Date createdTime;
}
