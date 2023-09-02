package com.tiktok.feign_util.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient("service-favorite")
public interface LikeFeignClient {
    @GetMapping("/douyin/favorite/like/count")
    Integer getLikeCount(@RequestParam("videoId") Long videoId);

    @GetMapping("/douyin/favorite/like/userCount")
    Integer getLikeCountByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/douyin/favorite/like/isFav")
    Boolean getIsLike(@RequestParam("userId") Long userId);

    @GetMapping("/douyin/favorite/inner/likedvideonum")
    Integer getLikedVideoNumByUserId(@RequestParam("userId") Long userId);
}
