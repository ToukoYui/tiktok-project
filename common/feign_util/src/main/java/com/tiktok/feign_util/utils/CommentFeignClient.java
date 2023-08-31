package com.tiktok.feign_util.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient("service-comment")
public interface CommentFeignClient {
    // 无token调用，内部用
    @GetMapping("/douyin/comment/inner/count")
    Integer getCommnetNumFromCommentModule(@RequestParam("videoId") Long videoId);
}
