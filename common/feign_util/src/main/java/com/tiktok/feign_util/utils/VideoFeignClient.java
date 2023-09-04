package com.tiktok.feign_util.utils;

import com.tiktok.model.vo.video.VideoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@FeignClient("service-feed")
public interface VideoFeignClient {
    @GetMapping("/douyin/inner/videonum")
    Integer getVideoNumByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/douyin/inner/videoinfolist")
    List<VideoVo> getVideoInfoList(@RequestParam("videoIdList") List<Long> videoIdList);

}
