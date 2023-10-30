package com.tiktok.service_comment.service;

import cn.hutool.core.bean.BeanUtil;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.entity.comment.Comment;
import com.tiktok.model.vo.comment.CommentVo;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.service_comment.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisTemplate<String, Long> redisTemplateLong;

    @Autowired
    private RedisTemplate<String, CommentVo> redisTemplateCommentVo;

    @Autowired
    private StringRedisTemplate redisTemplateString;

    /**
     * 发布评论
     * 缓存一致性：
     * 写操作：先写数据库，然后再删除缓存
     * 读操作：缓存命中则直接返回
     * 缓存未命中则查询数据库，并写入缓存，设定超时时间
     * @param userId
     * @param videoId
     * @param commentText
     */
    public CommentVo publishComment(Long userId, Long videoId, String commentText) {

        // 获取当前日期 mm-dd
        long current = System.currentTimeMillis();
        Date currentDate = new Date(current);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String date = simpleDateFormat.format(currentDate);

        Comment comment = new Comment(userId, videoId, commentText, date);
        // 存到数据库中
        // 使用mybatis提供的自增id策略,这样就不用再去访问数据库
        commentMapper.publishComment(comment);

        CommentVo commentVo = new CommentVo();
        BeanUtil.copyProperties(comment, commentVo);


        // 获取当前登录用户的信息
        UserVo userInfo = userFeignClient.userInfo(String.valueOf(userId));
        commentVo.setUser(userInfo);

        // 清空该视频的所有评论id列表缓存，但单个的具体评论不仍然留着
        String deleteKey = "comment:" + videoId + ":commentIdList:*";
        redisTemplateLong.delete(redisTemplateString.keys(deleteKey));

        return commentVo;
    }


    /**
     * 获取该视频的评论列表
     *
     *
     * @param videoId
     * @return
     */
    public List<CommentVo> getCommentList(Long videoId, Long start, Long end) {
        // 查询redis中是否有缓存
        String key = "comment:" + videoId + ":commentIdList:" + "index-" + start + "_" + end; // "comment:1:commentIdList:index-0_299“
        // 先拿前300条
        List<Long> commentIdListFromRedis = redisTemplateLong.opsForList().range(key, 0, 299);
        String keyPre = "comment:" + videoId + ":commentString:";
        if (commentIdListFromRedis.size() != 0) {
            log.info("获取视频评论id列表，从缓存中获取------------->" + commentIdListFromRedis);
            // 遍历id列表缓存，查询具体评论对象
            List<CommentVo> commentVoList = commentIdListFromRedis.stream().map((commentId) ->
                    redisTemplateCommentVo.opsForValue().get(keyPre + commentId)
            ).collect(Collectors.toList());
            return commentVoList;
        }

        // 缓存中没有,查询数据库中最新的【start，end】评论
        List<Comment> commentList = commentMapper.getCommentList(videoId, start, end);
        List<Long> commentIdListFromMysql = new ArrayList<>();
        // 封装数据
        List<CommentVo> commentVoList = new ArrayList<>();
        if (commentList.size() > 0) {
            commentVoList = commentList.stream().map(
                    (comment) -> {
                        String userId = comment.getUserId().toString();
                        // 获取用户信息
                        UserVo userInfo = userFeignClient.userInfo(userId);
                        CommentVo commentVo = new CommentVo();
                        BeanUtil.copyProperties(comment, commentVo);
                        commentVo.setUser(userInfo);
                        // 具体评论json单独存入redis中
                        redisTemplateCommentVo.opsForValue().set(keyPre + comment.getId(), commentVo, 24, TimeUnit.HOURS);
                        commentIdListFromMysql.add(comment.getId());
                        return commentVo;
                    }
            ).collect(Collectors.toList());
            // 将评论id列表存入redis中
            redisTemplateLong.opsForList().rightPushAll(key, commentIdListFromMysql);
            redisTemplateLong.expire(key,2,TimeUnit.HOURS);
        }
        // 评论
        return commentVoList;
    }

    /**
     * 获取视频的评论数
     *
     * @param videoId
     * @return
     */
    public Integer getCommentCount(Long videoId) {
        return commentMapper.queryCommentNumByVideoId(videoId);
    }

    /**
     * 删除某条评论
     *
     * @param commentId
     */
    public void deleteComment(Long commentId, Long videoId) {
        commentMapper.deleteCommentById(commentId);
        // 清空评论id列表缓存
        String deleteKey = "comment:" + videoId + ":commentIdList:*";
        redisTemplateLong.delete(redisTemplateString.keys(deleteKey));
    }
}
