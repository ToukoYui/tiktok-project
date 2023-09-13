package com.tiktok.model.entity.relation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Relation {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("follow_id")
    private Long followId;
    @JsonProperty("is_follow")
    private Boolean isFollow;
}
