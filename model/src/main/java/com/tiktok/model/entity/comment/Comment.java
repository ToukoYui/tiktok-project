package com.tiktok.model.entity.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Comment {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("video_id")
    private Long videoId;

    private String content;

    @JsonFormat(pattern = "mm-dd")
    @JsonProperty("create_date")
    private String create_date;

    public Comment(Long userId, Long videoId, String content, String create_date) {
        this.userId = userId;
        this.videoId = videoId;
        this.content = content;
        this.create_date = create_date;
    }
}
