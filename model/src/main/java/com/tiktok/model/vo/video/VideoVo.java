package com.tiktok.model.vo.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.vo.user.UserVo;
import lombok.Data;
import java.util.Date;

@Data
public class VideoVo {
    private Long id;
    private UserVo author;

    @JsonProperty("play_url")
    private String playUrl;

    @JsonProperty("cover_url")
    private String coverUrl;

    @JsonProperty("favorite_count")
    private Integer favoriteCount;

    @JsonProperty("comment_count")
    private Integer commentCount;

    @JsonProperty("is_favorite")
    private Boolean isFavorite;

    @JsonProperty("title")
    private String title;

    @JsonProperty("created_time")
    private Date createdTime;
}
