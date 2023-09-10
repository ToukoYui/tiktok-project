package com.tiktok.service_favorite.service;

import com.tiktok.feign_util.utils.VideoFeignClient;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.favorite.FavoriteResp;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_favorite.mapper.LikesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LikesService {
    @Autowired
    private LikesMapper likesMapper;

    @Resource
    private RedisTemplate<String, List<VideoVo>> redisTemplate;

    @Autowired
    private VideoFeignClient videoFeignClient;

    public FavoriteResp liked(Long videoId, String actionType, TokenAuthSuccess authSuccess) {
        try {
            String userId = authSuccess.getUserId();
            //查询用户是否点赞过该视频
            Integer count = likesMapper.selectIsLiked(Long.valueOf(userId), videoId);
            if (count == 0) {
                //未点赞过，添加新的点赞记录
                likesMapper.insertFavorite(videoId, 1, Long.valueOf(userId));
            } else {
                //点赞过，修改是否点赞即可
                if (actionType.equals("1")) {
                    likesMapper.updateFavorite(videoId, 1, Long.valueOf(userId));
                } else {
                    likesMapper.updateFavorite(videoId, 0, Long.valueOf(userId));
                }
            }
            // 点赞操作后删除该用户缓存
            redisTemplate.delete("user:" + userId);
            redisTemplate.delete("favoriteVideo:" + userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new FavoriteResp("500", "服务器出错，点赞失败哦~", null);
        }
        redisTemplate.delete("videolist:public");
        return new FavoriteResp("0", "点赞成功", null);
    }

    public FavoriteResp getLikedVideoList(String userId) {
        // 查询缓存中是否存在
        String key = "favoriteVideo:" + userId;
        if (redisTemplate.hasKey(key)){
            List<VideoVo> videoVoList = redisTemplate.opsForValue().get(key);
            log.info("获取喜欢视频列表------------>从缓存中获取");
            return new FavoriteResp("0", "获取喜欢视频列表成功", videoVoList);
        }
        // 查询用户点赞过的视频id列表
        List<Long> videoIdList = likesMapper.getVideoIdByUserId(Long.valueOf(userId));
        // 根据视频id列表获取视频详情列表
        List<VideoVo> videoInfoList = videoFeignClient.getVideoInfoList(videoIdList);
        // 存入redis中，2小时过期
        redisTemplate.opsForValue().set(key,videoInfoList,2, TimeUnit.HOURS);
        log.info("获取喜欢视频列表------------>从MYSQL中查询");
        return new FavoriteResp("0", "获取喜欢视频列表成功", videoInfoList);
    }
}
