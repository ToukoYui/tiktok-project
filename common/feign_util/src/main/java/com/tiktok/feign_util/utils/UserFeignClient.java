package com.tiktok.feign_util.utils;

import com.tiktok.model.vo.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("service-user")
public interface UserFeignClient {
    // 参数必须带上@RequestParam注解指明，否则无法识别
    @GetMapping("/douyin/user")
    UserVo getUserInfoFromUserModel(@RequestParam("user_id") String userId,@RequestParam("token") String token);
}
