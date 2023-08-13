package com.tiktok.service_user.controller;

import com.tiktok.service_user.config.TokenBacketLimiter;
import com.tiktok.service_user.model.vo.UserLoginResp;
import com.tiktok.service_user.model.vo.UserRegisterResp;
import com.tiktok.service_user.model.vo.UserResp;
import com.tiktok.service_user.model.vo.UserVo;
import com.tiktok.service_user.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserRegisterResp userRegister(String username,String password){
        return userService.userRegister(username,password);
    }

    @PostMapping("/login")
    public UserLoginResp userLogin(String username, String password){
        // 令牌桶限流登录请求
        boolean isGot = TokenBacketLimiter.getToken();
        if (!isGot){
            return new UserLoginResp("429","请求频繁，请稍后再试",null,null);
        }
        return userService.userLogin(username, password);
    }

    @GetMapping
    public UserResp userInfo(@Param("user_id") Long userId, Boolean isPermitted){
        System.out.println("userId = " + userId);
        System.out.println("isPermitted = " + isPermitted);
        if (!isPermitted){
            return new UserResp("403","token错误，禁止访问",null);
        }
        // todo
        UserVo userVo = new UserVo();
        return new UserResp("200",null,userVo);
    }


}
