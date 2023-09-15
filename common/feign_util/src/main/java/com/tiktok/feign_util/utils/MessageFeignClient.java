package com.tiktok.feign_util.utils;

import com.tiktok.model.vo.message.MessageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient("service-message")
public interface MessageFeignClient {

    @GetMapping("douyin/message/inner/latest")
    MessageVo getLatestMessage(@RequestParam("userId") Long userId, @RequestParam("friendId") Long friendId);
}
