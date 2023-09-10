package com.tiktok.feign_util.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient("service-relation")
public interface RelationFeignClient {

    @GetMapping("douyin/relation/inner/follow/count")
    Integer getFollowUserCount(@RequestParam("userId") Long userId);

    @GetMapping("douyin/relation/inner/follower/count")
    Integer getFollowerCount(@RequestParam("userId") Long userId);

    @GetMapping("douyin/relation/inner/follow/relationship")
    boolean getIsRelated(@RequestParam("authorId") Long authorId,@RequestParam("userId") Long userId);
}
