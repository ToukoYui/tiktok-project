package com.tiktok.service_relation.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.relation.MutualFollowResp;
import com.tiktok.model.vo.relation.RelationResp;
import com.tiktok.service_relation.mapper.RelationMapper;
import com.tiktok.service_relation.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @Autowired
    private RelationMapper relationMapper;

    /**
     * 登录用户对其他用户进行关注或取消关注
     *
     * @param toUserId
     * @param actionType
     * @param tokenAuthSuccess
     * @return
     */
    @PostMapping("/action")
    public RelationResp relate(@RequestParam("to_user_id") Long toUserId, @RequestParam("action_type") String actionType, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new RelationResp("500", "请先登录哦~", null);
        }
        return relationService.related(toUserId, actionType, tokenAuthSuccess);
    }


    /**
     * 登录用户关注的所有用户列表
     *
     * @param userId
     * @param tokenAuthSuccess
     * @return
     */
    @GetMapping("/follow/list")
    public RelationResp getRelatedUserList(@RequestParam("user_id") Long userId, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new RelationResp("500", "请先登录哦~", null);
        }
        return relationService.getRelatedUserList(userId);
    }


    /**
     * 所有关注登录用户的粉丝列表
     * @param userId
     * @param tokenAuthSuccess
     * @return
     */
    @GetMapping("/follower/list")
    public RelationResp getFollowerList(@RequestParam("user_id") Long userId, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess) {
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new RelationResp("500", "请先登录哦~", null);
        }
        return relationService.getFollowerList(userId);
    }


    /**
     * 互相关注的用户列表
     * @param userId
     * @param tokenAuthSuccess
     * @return
     */
    @GetMapping("/friend/list")
    public MutualFollowResp getFriendUserList(@RequestParam("user_id") Long userId, @TokenAuthAnno TokenAuthSuccess tokenAuthSuccess){
        if (tokenAuthSuccess == null || !tokenAuthSuccess.getIsSuccess()) {
            return new MutualFollowResp("500", "请先登录哦~", null);
        }
        return relationService.getFriendUserList(userId);
    }



    /**
     * 内部接口,无需token调用
     * 获取用户的关注用户数
     *
     * @param userId
     * @return
     */
    @GetMapping("/inner/follow/count")
    Integer getFollowUserCount(@RequestParam("userId") Long userId) {
        return relationMapper.selectFollowUserCount(userId);
    }

    /**
     * 内部接口,无需token调用
     * 获取用户的粉丝数
     *
     * @param userId
     * @return
     */
    @GetMapping("/inner/follower/count")
    Integer getFollowerCount(@RequestParam("userId") Long userId) {
        return relationMapper.selectFollowerCount(userId);
    }


}
