package com.tiktok.service_feed.controller;

import com.tiktok.model.anno.OptionalParamAnno;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    public VideoResp getVideoList(@RequestParam("latest_time") String latestTimeStr, @OptionalParamAnno TokenAuthSuccess tokenAuthSuccess) {
        if (!tokenAuthSuccess.getIsSuccess()) {
            return new VideoResp("403", "token错误，禁止访问", null, null);
        }
        // 如果last_time为空则用当前时间字符串
        Timestamp timestamp = StringUtils.isEmpty(latestTimeStr) ?
                new Timestamp(System.currentTimeMillis()) : new Timestamp(Long.parseLong(latestTimeStr));
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        String lastTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
        // 获取视频
        List<VideoVo> videoVoList = videoService.getVideoList(lastTime, tokenAuthSuccess.getToken(),tokenAuthSuccess.getUserId());
        // 获取最早发布的视频的发布时间
        Date nextTime = videoVoList.get(videoVoList.size() - 1).getCreatedTime();
        Integer nextTimeInt = Math.toIntExact(nextTime.getTime() / 1000);
        return new VideoResp("0", "获取视频流成功", nextTimeInt, videoVoList);
    }

    /**
     * 视频上传
     *
     * @param multipartFile
     * @param title
     * @param tokenAuthSuccess
     * @return
     */
    @PostMapping("/publish/action")
    public PublishResp publishVideo(@RequestParam("data") MultipartFile multipartFile, String title, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (!tokenAuthSuccess.getIsSuccess()) {
            return new PublishResp(403, "token错误，禁止访问");
        }
        String videoUrl = videoService.uploadVideo(multipartFile, title);
        if (StringUtils.isEmpty(videoUrl)) {
            return new PublishResp(400, "视频发布失败");
        }
        log.info("视频上传成功，上传路径为----------------->" + videoUrl);
        return new PublishResp(200, "视频发布成功");
    }
}
