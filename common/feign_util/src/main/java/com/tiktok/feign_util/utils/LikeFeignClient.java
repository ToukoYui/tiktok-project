package com.tiktok.feign_util.utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface LikeFeignClient {
    @GetMapping("/douyin/like/inner/count")
    Integer getLikeCount(@RequestParam("videoId") Long videoId);
    @GetMapping("/douyin/like/inner/userCount")
    Integer getLikeCountByUserId(@RequestParam("userId") Long userId);
    @GetMapping("/douyin/like/inner/isFav")
    Boolean getIsLike(@RequestParam("userId") Long userId);
}
