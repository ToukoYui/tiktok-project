package com.tiktok.service_comment.service;

import cn.hutool.core.bean.BeanUtil;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.entity.comment.Comment;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.comment.CommentVo;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.model.vo.video.VideoVo;
import com.tiktok.service_comment.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    @Resource
    private RedisTemplate<String, List<CommentVo>> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发布评论
     * 缓存一致性：
     * 写操作：先写数据库，然后再删除缓存
     * 读操作：缓存命中则直接返回
     * 缓存未命中则查询数据库，并写入缓存，设定超时时间
     *
     * @param tokenAuthSuccess
     * @param videoId
     * @param commentText
     */
    public CommentVo publishComment(TokenAuthSuccess tokenAuthSuccess, Long videoId, String commentText) {
        // 获取当前登录用户id
        String token = tokenAuthSuccess.getToken();
        String userId = stringRedisTemplate.opsForValue().get("user:token:" + token);

        // 获取当前日期 mm-dd
        long current = System.currentTimeMillis();
        Date currentDate = new Date(current);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String date = simpleDateFormat.format(currentDate);

        Comment comment = new Comment(Long.valueOf(userId), videoId, commentText, date);
        // 存到数据库中
        // 使用mybatis提供的自增id策略,这样就不用再去访问数据库
        commentMapper.publishComment(comment);

        CommentVo commentVo = new CommentVo();
        BeanUtil.copyProperties(comment, commentVo);

        UserVo userInfo;
        // 获取当前登录用户的信息
        // token是有可能为空的
        if (StringUtils.isEmpty(token)) {
            userInfo = userFeignClient.getUserInfoFromUserModelByNotToken(userId);
        } else {
            userInfo = userFeignClient.getUserInfoFromUserModel(userId, token).getUserVo();
        }
        commentVo.setUser(userInfo);

        // 删除缓存
        redisTemplate.delete("commentlist:" + videoId);

        return commentVo;
    }


    /**
     * 获取该视频的评论列表
     *
     * @param tokenAuthSuccess
     * @param videoId
     * @return
     */
    public List<CommentVo> getCommentList(TokenAuthSuccess tokenAuthSuccess, Long videoId) {
        // 查询redis中是否有缓存
        String key = "commentlist:" + videoId;
        List<CommentVo> commentVos = redisTemplate.opsForValue().get(key);
        if (commentVos != null) {
            log.info("获取视频评论，从缓存中获取------------->" + commentVos.toString());
            return commentVos;
        }

        // 缓存中没有,查询数据库
        List<Comment> commentList = commentMapper.getCommentList(videoId);
        // 封装数据
        List<CommentVo> commentVoList = commentList.stream().map(
                (comment) -> {
                    String userId = comment.getUserId().toString();
                    // 获取用户信息
                    UserVo userInfo = userFeignClient.getUserInfoFromUserModelByNotToken(userId);
                    CommentVo commentVo = new CommentVo();
                    BeanUtil.copyProperties(comment, commentVo);
                    commentVo.setUser(userInfo);
                    // todo 评论点赞数量
                    return commentVo;
                }
        ).collect(Collectors.toList());
        // 存入redis中
        redisTemplate.opsForValue().set(key, commentVoList, 3, TimeUnit.MINUTES);
        return commentVoList;
    }
}
