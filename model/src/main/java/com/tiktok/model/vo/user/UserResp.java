package com.tiktok.model.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResp {
    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("user")
    private UserVo userVo;

    public UserResp(String statusCode, String statusMsg, UserVo userVo) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.userVo = userVo;
    }
}
