package com.tiktok.service_favorite.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.favorite.FavoriteResp;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_favorite.mapper.LikesMapper;
import com.tiktok.service_favorite.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private LikesService likesService;

    @Autowired
    private LikesMapper likesMapper;

    @PostMapping("/action")
    public FavoriteResp like(@RequestParam("video_id") Long videoId, @RequestParam("action_type") String actionType, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (!tokenAuthSuccess.getIsSuccess()) {
            return new FavoriteResp("500", "请先登录哦~", null);
        }
        return likesService.liked(videoId, actionType, tokenAuthSuccess);
    }

    @GetMapping("/like/count")
    Integer getLikeCount(@RequestParam("videoId") Long videoId) {
        return likesMapper.getLikeCount(videoId);
    }

    @GetMapping("/like/userCount")
    Integer getLikeCountByUserId(@RequestParam("userId") Long userId) {
        return likesMapper.getLikeCountByUserId(userId);
    }

    @GetMapping("/like/isFav")
    Boolean getIsLike(@RequestParam("userId") Long userId, @RequestParam("videoId") Long videoId) {
        Integer isLike = likesMapper.getIsLike(userId, videoId);
        return isLike != 0;
    }

    @GetMapping("/inner/likedvideonum")
    public Integer getLikedVideoNumByUserId(@RequestParam("userId") Long userId) {
        return likesService.getLikedVideoNumByUserId(userId);
    }
}
