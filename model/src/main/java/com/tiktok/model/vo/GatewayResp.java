package com.tiktok.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayResp {
    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;
}
