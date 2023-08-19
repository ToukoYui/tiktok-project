package com.tiktok.service_user.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.video.VideoResp;
import com.tiktok.service_user.config.TokenBacketLimiter;
import com.tiktok.service_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tiktok.model.vo.user.UserLoginResp;
import com.tiktok.model.vo.user.UserRegisterResp;
import com.tiktok.model.vo.user.UserResp;
import com.tiktok.model.vo.user.UserVo;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserRegisterResp userRegister(String username, String password){
        return userService.userRegister(username,password);
    }

    @PostMapping("/login")
    public UserLoginResp userLogin(String username, String password){
        // 令牌桶限流登录请求
        boolean isGot = TokenBacketLimiter.getToken();
        if (!isGot){
            return new UserLoginResp(429,"请求频繁，请稍后再试",null,null);
        }
        return userService.userLogin(username, password);
    }

    /**
     * 需要注意缓存更新策略
     * 查询时，如果缓存未命中,则查询数据库,将数据库结果写入缓存,并设置超时时间
     * 修改时,先修改数据库,再删除缓存
     * @param userId
     * @param tokenAuthSuccess
     * @return
     */
    @GetMapping
    public UserResp userInfo(@RequestParam("user_id") Long userId, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess){
        if (tokenAuthSuccess == null) {
            return new UserResp("403","token错误，禁止访问",null);
        }
        UserVo userVo = userService.getUserInfo(userId);
        // 获取不到user,说明userId有误
        if(userVo.getUsername() == null){
            return new UserResp("403","输入userId有误,请重新输入",null);
        }
        return new UserResp("0","获取用户信息成功",userVo);
    }


    /**
     * 无需token验证，供feed调用
     * @param userId
     * @return
     */
    @GetMapping("/inner")
    public UserResp userInfo(@RequestParam("user_id") Long userId){
        UserVo userVo = userService.getUserInfo(userId);
        // 获取不到user,说明userId有误
        if(userVo.getUsername() == null){
            return new UserResp("403","输入userId有误,请重新输入",null);
        }
        return new UserResp("0","获取用户信息成功",userVo);
    }


    // todo 软件测试---垃圾电脑
    @GetMapping("/logout")
    public UserResp userLogout(NativeWebRequest nativeWebRequest) {
        boolean result = userService.userLogout(nativeWebRequest);
        if (result) {
            return new UserResp("200", "注销成功", null);
        }
        // 注销失败
        return new UserResp("200", "注销失败", null);
    }

}
