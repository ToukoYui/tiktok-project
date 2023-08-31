package com.tiktok.service_favorite.service;

import com.tiktok.feign_util.utils.VideoFeignClient;
import com.tiktok.model.entity.video.Video;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.favorite.FavoriteResp;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_favorite.mapper.LikesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikesService {
    @Resource
    private RedisTemplate<String, List<VideoVo>> redisTemplate;
    @Autowired
    private VideoFeignClient videoFeignClient;
    @Autowired
    private LikesMapper likesMapper;
    public FavoriteResp liked(Long videoId, String actionType) {
        try {
            if(actionType.equals("1")){
                likesMapper.updateFavorite(videoId,1);
            }else {
                likesMapper.updateFavorite(videoId,0);
            }
        }catch (Exception e){
            return new FavoriteResp("500","服务器出现错误，点赞失败~",null);
        }
        return new FavoriteResp("0","点赞成功",null);
    }
}
