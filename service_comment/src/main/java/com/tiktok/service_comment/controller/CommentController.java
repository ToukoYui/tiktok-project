package com.tiktok.service_comment.controller;

import com.tiktok.model.vo.comment.CommentActionResp;
import com.tiktok.model.vo.comment.CommentListResp;
import com.tiktok.model.vo.comment.CommentVo;
import com.tiktok.service_comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisTemplate<String, Long> redisTemplateLong;

    /**
     * 登录用户对视频进行评论
     *
     * @param videoId
     * @param actionType
     * @param commentText
     * @return
     */
    @PostMapping("/action")
    public CommentActionResp getComment(
                                        @RequestParam("video_id") Long videoId,
                                        @RequestParam("action_type") int actionType,
                                        @RequestParam(value = "comment_text",required = false) String commentText,
                                        @RequestParam(value = "comment_id",required = false) String commentId
    ) {
        CommentVo commentVo = null;

        if (actionType == 1) {
            // 发布评论
            Long userId = redisTemplateLong.opsForValue().get("auth::userId");
            commentVo = commentService.publishComment(userId, videoId, commentText);
        } else {
            commentService.deleteComment(Long.parseLong(commentId),videoId);
        }
        return new CommentActionResp("0", actionType == 1 ? "评论成功" : "评论删除成功", commentVo);
    }


    /**
     * 视频评论列表
     * 查看该视频的所有评论,按发布时间倒序
     *
     *
     * @param videoId
     * @return
     */
    @GetMapping("/list")
    public CommentListResp getCommentList(@RequestParam("video_id") Long videoId) {
        Long start = 0L, end = 299L;
        // 不需要身份验证,任何人都可以查看评论
        List<CommentVo> commentVoList = commentService.getCommentList(videoId, start, end);
        return new CommentListResp("0", "获取评论列表成功", commentVoList);
    }

    /**
     * 内部接口，查询某个视频的评论数
     *
     * @param videoId
     * @return
     */
    @GetMapping("/inner/count")
    public Integer getCommentNum(Long videoId) {
        return commentService.getCommentCount(videoId);
    }
}
