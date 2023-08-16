package com.tiktok.service_feed.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.video.VideoResp;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_feed.service.VideoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;

    /**
     * 获取视频流信息（包含作者信息）
     * @param latestTimeStr 时间戳字符串，处理时要转为DateTime类型
     * @param tokenAuthSuccess      从token中解析出来的
     * @return
     */
    @GetMapping("/feed")
    public VideoResp getVideoList(@RequestParam("latest_time") String latestTimeStr, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null) {
            return new VideoResp("403", "token错误，禁止访问", null, null);
        }
        // 如果last_time为空则用当前时间
        Timestamp timestamp = StringUtils.isEmpty(latestTimeStr) ?
                new Timestamp(System.currentTimeMillis()) :
                new Timestamp(Long.parseLong(latestTimeStr));
        LocalDateTime lastTime = timestamp.toLocalDateTime();
        List<VideoVo> videoList =
                videoService.getVideoList(latestTimeStr,lastTime, tokenAuthSuccess.getUserId(),tokenAuthSuccess.getToken());
        return new VideoResp("200","获取视频流成功",null,videoList);
    }
}
