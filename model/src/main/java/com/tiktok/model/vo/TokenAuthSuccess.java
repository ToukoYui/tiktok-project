package com.tiktok.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenAuthSuccess {
    private String userId;
    private String token;
    private Boolean isSuccess;
}
