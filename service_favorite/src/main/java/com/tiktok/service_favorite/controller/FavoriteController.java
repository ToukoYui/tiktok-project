package com.tiktok.service_favorite.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.favorite.FavoriteResp;
import com.tiktok.service_favorite.mapper.LikesMapper;
import com.tiktok.service_favorite.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private LikesService likesService;

    @Autowired
    private LikesMapper likesMapper;


    @PostMapping("/action")
    public FavoriteResp like(@RequestParam("video_id") Long videoId, @RequestParam("action_type") String actionType,@TokenAuthAnno Long userId) {
        return likesService.liked(videoId, actionType, userId);
    }
    /**
     * 根据视频id获取用户的喜欢视频数量
     * 内部接口，供user模块调用
     * videoId
     * @param
     * @return
     */
    @GetMapping("/like/count")
    Integer getLikeCountByVideoId(@RequestParam("videoId") Long videoId) {
        return likesMapper.getLikeCountByVideoId(videoId);
    }

    /**
     * 根据userId获取用户的喜欢视频数量
     * 内部接口，供user模块调用
     *
     * @param userId
     * @return
     */
    @GetMapping("/like/userCount")
    Integer getLikeCountByUserId(@RequestParam("userId") Long userId) {
        return likesMapper.getLikeCountByUserId(userId);
    }
    /**
     * 根据用户id和视频id获取用户的喜欢视频数量
     * 内部接口，供user模块调用
     *
     * @param userId
     * @param videoId
     * @return
     */
    @GetMapping("/like/isFav")
    Boolean getIsLike(@RequestParam("userId") Long userId, @RequestParam("videoId") Long videoId) {
        Integer isLike = likesMapper.getIsLike(userId, videoId);
        if (isLike == null){
            return false;
        }
        return isLike != 0;
    }


    @GetMapping("/list")
    public FavoriteResp getLikedVideoList(@RequestParam("user_id") String userId) {
        return likesService.getLikedVideoList(userId);
    }


}
