package com.tiktok.service_user.model.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
}