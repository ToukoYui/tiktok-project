package com.tiktok.service_user.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenToUserId;
import com.tiktok.service_user.config.TokenBacketLimiter;
import com.tiktok.service_user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.tiktok.model.vo.user.UserLoginResp;
import com.tiktok.model.vo.user.UserRegisterResp;
import com.tiktok.model.vo.user.UserResp;
import com.tiktok.model.vo.user.UserVo;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/register")
    public UserRegisterResp userRegister(String username, String password) {
        return userService.userRegister(username, password);
    }

    @PostMapping("/login")
    public UserLoginResp userLogin(String username, String password) {
        // 令牌桶限流登录请求
        boolean isGot = TokenBacketLimiter.getToken();
        if (!isGot) {
            return new UserLoginResp(429, "请求频繁，请稍后再试", null, null);
        }
        return userService.userLogin(username, password);
    }

    /**
     * 需要注意缓存更新策略
     * 查询时，如果缓存未命中,则查询数据库,将数据库结果写入缓存,并设置超时时间
     * 修改时,先修改数据库,再删除缓存
     *
     * @param userId
     * @return
     */
    @GetMapping
    public UserResp getUserInfo(@RequestParam("user_id") Long userId) {
        UserVo userVo = userService.getUserInfo(userId);
        // 获取不到user,说明userId有误
        if (userVo.getUsername() == null) {
            return new UserResp("403", "输入userId有误,请重新输入", null);
        }
        return new UserResp("0", "获取用户信息成功", userVo);
    }


    /**
     * 无需token验证，供feed调用
     *
     * @param userId
     * @return
     */
    @GetMapping("/inner")
    public UserVo userInfo(@RequestParam("user_id") Long userId) {
        log.info("被video调用");
        try {
            UserVo userVo = userService.getUserInfo(userId);
            // 获取不到user,说明userId有误
            if (userVo.getUsername() == null) {
                log.info("内部接口获取用户信息失败,对象为空");
                return null;
            }
            return userVo;
        } catch (Exception e) {
            log.info("内部接口获取用户信息失败-------->" + e.getMessage());
        }
        return null;
    }


    @GetMapping("/logout")
    public UserResp userLogout(NativeWebRequest nativeWebRequest) {
        //退出登录需要删除视频缓存
        redisTemplate.delete("videolist:public");
        return new UserResp("0", "注销成功", null);
    }


    /**
     * 根据用户id列表查询用户信息列表
     * 内部接口,供relation模块使用
     *
     * @param userIdList
     * @return
     */
    @GetMapping("/inner/userinfolist")
    public List<UserVo> getUserInfoList(@RequestParam("userIdList") List<Long> userIdList,@RequestParam("userId") Long userId) {
        return userService.getUserInfoList(userIdList,userId);
    }


}
