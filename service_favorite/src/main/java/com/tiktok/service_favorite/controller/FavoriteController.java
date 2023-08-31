package com.tiktok.service_favorite.controller;

import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.favorite.FavoriteResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @PostMapping("/action")
    public FavoriteResp like(@RequestParam("video_id")Long videoId, @RequestParam("action_type") String actionType,@TokenAuthAnno TokenAuthSuccess tokenAuthSuccess){
        if(!tokenAuthSuccess.getIsSuccess()){
            return new FavoriteResp("403","请先登录哦~",null);
        }

        return null;
    }
}
