package com.tiktok.service_user.controller;

import com.tiktok.service_user.model.vo.UserRegisterReq;
import com.tiktok.service_user.model.vo.UserRegisterResp;
import com.tiktok.service_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserRegisterResp userRegister(UserRegisterReq registerReq){
        return userService.userRegister(registerReq);
    }

    @PostMapping("/login")
    public UserRegisterResp userLogin(UserRegisterReq registerReq){
        return userService.userLogin(registerReq);
    }


}
