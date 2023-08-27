package com.tiktok.model.entity.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
    @JsonProperty("created_date")
    private String createdDate;

    public Comment(Long userId, Long videoId, String content, String createdDate) {
        this.userId = userId;
        this.videoId = videoId;
        this.content = content;
        this.createdDate = createdDate;
    }
}
