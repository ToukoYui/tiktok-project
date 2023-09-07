package com.tiktok.model.vo.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tiktok.model.vo.user.UserVo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


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

    @Builder.Default
    @JsonProperty("is_favorite")
    private Boolean isFavorite = false;

    @JsonProperty("title")
    private String title;


    @JsonProperty("created_time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;

    public VideoVo(Long id, UserVo author, String playUrl, String coverUrl, Integer favoriteCount, Integer commentCount, Boolean isFavorite, String title, LocalDateTime createdTime) {
        this.id = id;
        this.author = author;
        this.playUrl = playUrl;
        this.coverUrl = coverUrl;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.isFavorite = isFavorite;
        this.title = title;
        this.createdTime = createdTime;
    }

    public VideoVo() {
    }
}
