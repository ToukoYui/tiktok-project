package com.tiktok.feign_util.utils;

import com.tiktok.model.vo.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient("service-user")
public interface UserFeignClient {
    // 无token调用，内部用
    @GetMapping("/douyin/user/inner")
    UserVo userInfo(@RequestParam("user_id") String userId);

    // 无token调用,内部用
    @GetMapping("/douyin/user/inner/userinfolist")
    List<UserVo> getUserInfoList(@RequestParam("userIdList") List<Long> userIdList);

}
