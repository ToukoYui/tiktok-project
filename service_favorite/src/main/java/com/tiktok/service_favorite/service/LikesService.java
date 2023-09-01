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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikesService {
    @Autowired
    private LikesMapper likesMapper;
    @Resource
    private RedisTemplate<String, List<VideoVo>> redisTemplate;
    public FavoriteResp liked(Long videoId, String actionType,TokenAuthSuccess authSuccess) {
        try {
            String userId = authSuccess.getUserId();
            //查询用户是否点赞过该视频
            Integer count = likesMapper.selectLike(userId,videoId);
            if(count == 0){
                //未点赞过，添加新的点赞记录
                likesMapper.insertFavorite(videoId,1,userId);
            }else {
                //点赞过，修改是否点赞即可
                if(actionType.equals("1")){
                    likesMapper.updateFavorite(videoId,1,userId);
                }else {
                    likesMapper.updateFavorite(videoId,0,userId);
                }
            }
        }catch (Exception e){
            return new FavoriteResp("500","服务器出错，点赞失败哦~",null);
        }
        redisTemplate.delete("videolist:public");
        return new FavoriteResp("0","点赞成功",null);
    }
}
