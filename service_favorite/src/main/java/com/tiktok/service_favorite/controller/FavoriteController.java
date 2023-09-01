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
    @Resource
    private RedisTemplate<String, List<VideoVo>> redisTemplate;
    @Autowired
    private LikesMapper likesMapper;
    @PostMapping("/action")
    public FavoriteResp like(@RequestParam("video_id")Long videoId, @RequestParam("action_type") String actionType,@TokenAuthAnno TokenAuthSuccess tokenAuthSuccess){
        if(!tokenAuthSuccess.getIsSuccess()){
            return new FavoriteResp("403","请先登录哦~",null);
        }
        redisTemplate.delete("videolist:public");
        return likesService.liked(videoId,actionType);
    }
    @GetMapping("/douyin/like/count")
    Integer getLikeCount(@RequestParam("videoId") Long videoId){
        return likesMapper.getLikeCount(videoId);
    }
    @GetMapping("/douyin/like/userCount")
    Integer getLikeCountByUserId(@RequestParam("userId") Long userId){
        return likesMapper.getLikeCountByUserId(userId);
    }
    @GetMapping("/douyin/like/isFav")
    Boolean getIsLike(@RequestParam("userId") Long userId){
        Integer isLike = likesMapper.getIsLike(userId);
        return isLike != 0;
    }
}
