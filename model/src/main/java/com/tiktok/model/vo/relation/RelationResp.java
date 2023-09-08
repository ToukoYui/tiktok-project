package com.tiktok.model.vo.relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tiktok.model.vo.user.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RelationResp {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_msg")
    private String statusMsg;
    @JsonProperty("user_list")
    private List<UserVo> userVoList;
}
