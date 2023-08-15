package com.tiktok.service_feed.controller;

import com.tiktok.model.vo.video.VideoResp;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/video")
public class VideoController {
    /**
     * 获取视频流信息（包含作者信息）
     * @param lastTimeStr 时间戳字符串，处理时要转为DateTime类型
     * @param token
     * @return
     */
    @GetMapping("/feed")
    public VideoResp getVideoList(@RequestParam("last_time")String lastTimeStr, String token){
        // 如果last_time为空则用当前时间
        Timestamp timestamp = StringUtils.isEmpty(lastTimeStr)?
                new Timestamp(System.currentTimeMillis()):
                new Timestamp(Long.parseLong(lastTimeStr));
        LocalDateTime lastTime = timestamp.toLocalDateTime();
        // todo service
        return null;
    }
}
