package com.tiktok.service_user.service;

import com.tiktok.model.entity.user.User;
import com.tiktok.service_user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GptUserCreSevice {

    @Autowired
    private UserMapper userMapper;

    @Async
    public void asyncMethod() {
        User user = new User();
        user.setId(7l);
        user.setUsername("GPT");
        userMapper.insert(user);
    }
}
