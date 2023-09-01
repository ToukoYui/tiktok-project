package com.tiktok.model.entity.like;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Likes {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("video_id")
    private Long videoId;
    @JsonProperty("is_favorite")
    private Boolean isFavorite;
}
