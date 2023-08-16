package com.tiktok.service_feed.service;

import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.feign_util.utils.VideoFeignClient;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.model.vo.video.VideoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoService {
    @Autowired
    private UserFeignClient userFeignClient;

    public List<VideoVo> getVideoList(String latestTimeStr, LocalDateTime lastTime, String userId, String token) {
        // 获取用户信息
        UserVo userInfo = userFeignClient.getUserInfoFromUserModel(userId, token);
        return null;
    }
}
