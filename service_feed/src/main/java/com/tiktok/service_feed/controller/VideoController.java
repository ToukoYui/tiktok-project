package com.tiktok.service_feed.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.video.PublishResp;
import com.tiktok.model.vo.video.VideoResp;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_feed.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;

    /**
     * 获取视频流信息（包含作者信息）
     *
     * @param latestTimeStr    时间戳字符串，处理时要转为DateTime类型
     * @param tokenAuthSuccess 从token中解析出来的
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
                videoService.getVideoList(latestTimeStr, lastTime, tokenAuthSuccess.getUserId(), tokenAuthSuccess.getToken());
        return new VideoResp("200", "获取视频流成功", null, videoList);
    }

    @PostMapping("/publish/action")
    public PublishResp publishVideo(@RequestParam("data") MultipartFile multipartFile, String title, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        String videoUrl = videoService.uploadVideo(multipartFile, title);
        if (StringUtils.isEmpty(videoUrl)) {
            return new PublishResp(400, "视频发布失败");
        }
        log.info("视频上传成功，上传路径为----------------->" + videoUrl);
        return new PublishResp(200, "视频发布成功");
    }
}
