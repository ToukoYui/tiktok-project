package com.tiktok.model.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.vo.user.FriendUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MutualFollowResp {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    @JsonProperty("user_list")
    private List<FriendUser> friendUserList;
}
