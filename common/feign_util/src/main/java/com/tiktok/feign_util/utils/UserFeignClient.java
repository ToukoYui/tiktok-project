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
    // 参数必须带上@RequestParam注解指明，否则无法识别
    // 这里的返回值类型不一样
    @GetMapping("/douyin/user")
    UserResp getUserInfoFromUserModel(@RequestParam("user_id") String userId, @RequestParam("token") String token);

    // 无token调用，内部用
    @GetMapping("/douyin/user/inner")
    UserVo getUserInfoFromUserModelByNotToken(@RequestParam("user_id") String userId);
}
