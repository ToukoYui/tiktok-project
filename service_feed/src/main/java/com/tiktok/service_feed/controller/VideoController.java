package com.tiktok.service_feed.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenToUserId;
import com.tiktok.model.vo.video.PublishResp;
import com.tiktok.model.vo.video.VideoResp;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_feed.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maximumFileSize;

    /**
     * 获取视频流信息（包含作者信息）
     *
     * @param latestTimeStr 时间戳字符串，处理时要转为DateTime类型
     * @return
     */
    @GetMapping("/feed")
    public VideoResp getVideoList(@RequestParam("latest_time") String latestTimeStr,@TokenAuthAnno TokenToUserId tokenToUserId) {
        // 如果last_time为空则用当前时间字符串
        Timestamp timestamp = StringUtils.isEmpty(latestTimeStr) ?
                new Timestamp(System.currentTimeMillis()) : new Timestamp(Long.parseLong(latestTimeStr));
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        String lastTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
        // 获取视频
        List<VideoVo> videoVoList = videoService.getVideoList(lastTime,tokenToUserId);
        // 获取最早发布的视频的发布时间
        LocalDateTime nextTime = videoVoList.get(videoVoList.size() - 1).getCreatedTime();
        Integer nextTimeInt = Math.toIntExact(nextTime.toEpochSecond(ZoneOffset.of("+8")));
        return new VideoResp("0", "获取视频流成功", nextTimeInt, videoVoList);
    }

    /**
     * 视频上传
     *
     * @param multipartFile
     * @param title
     * @return
     */
    @PostMapping("/publish/action")
    public PublishResp publishVideo(@TokenAuthAnno TokenToUserId tokenToUserId,@RequestParam("data") MultipartFile multipartFile, String title) {
        Long userId = tokenToUserId.getUserId();
        try {
            String videoUrl = videoService.uploadVideo(multipartFile, title, userId);
            if (StringUtils.isEmpty(videoUrl)) {
                return new PublishResp(400, "视频发布失败");
            }
            log.info("视频上传成功，上传路径为----------------->" + videoUrl);
        } catch (Exception e) {
            if (e instanceof FileUploadBase.FileSizeLimitExceededException) {
                return new PublishResp(0, "发布视频大小超过" + maximumFileSize + "，发布失败");
            } else {
                log.error(e.getMessage());
            }
        }
        return new PublishResp(0, "视频发布成功");
    }


    /**
     * 获取登录用户发布的视频列表,直接列出当前用户所有投稿过的视频
     *
     *
     * @return
     */
    @GetMapping("/publish/list")
    public VideoResp getMyVideoList(@TokenAuthAnno TokenToUserId tokenToUserId) {
        Long userId = tokenToUserId.getUserId();
        // 获取当前用户发布的视频,并返回
        List<VideoVo> myVideoList = videoService.getMyVideoList(userId);
        return new VideoResp("0", "获取当前用户视频成功", null, myVideoList);
    }

    /**
     * 获取用户的发布视频数量
     * 内部接口，供user模块调用
     *
     * @param userId
     * @return
     */
    @GetMapping("/inner/videonum")
    public Integer getVideoNumByUserId(@RequestParam("userId") Long userId) {
        return videoService.getVideoNumByUserId(userId);
    }

    /**
     * 根据视频id列表查询视频信息列表
     * 内部接口，供favorite模块调用
     *
     * @param videoIdList
     * @return
     */
    @GetMapping("/inner/videoinfolist")
    public List<VideoVo> getVideoInfoList(@RequestParam("videoIdList") List<Long> videoIdList) {
        return videoService.getVideoInfoList(videoIdList);
    }
}
