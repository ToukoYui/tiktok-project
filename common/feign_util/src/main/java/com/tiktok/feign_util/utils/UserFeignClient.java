package com.tiktok.feign_util.utils;

import com.tiktok.model.vo.user.UserResp;
import com.tiktok.model.vo.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("service-user")
public interface UserFeignClient {
    // 无token调用，内部用
    @GetMapping("/douyin/user/inner")
    UserVo userInfo(@RequestParam("user_id") String userId);
}
