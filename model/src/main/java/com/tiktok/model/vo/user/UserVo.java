package com.tiktok.model.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserVo {
    private Long id;

    @JsonProperty("name")
    private String username;

    private String avatar;

    @JsonProperty("follow_count")
    private Integer followCount;

    @JsonProperty("follower_count")
    private Integer followerCount;

    @JsonProperty("is_follow")
    private Boolean isFollow;

    @JsonProperty("total_favorited")
    private Integer totalFavoritedCount;

    @JsonProperty("work_count")
    private Integer workCount;

    @JsonProperty("favorite_count")
    private Integer favoriteCount;

    public UserVo() {
    }
}
