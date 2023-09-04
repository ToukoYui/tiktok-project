package com.tiktok.service_feed.service;

import com.sun.org.apache.bcel.internal.generic.FSUB;
import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.feign_util.utils.CommentFeignClient;
import com.tiktok.feign_util.utils.LikeFeignClient;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class AsyncService {
    @Autowired
    private CommentFeignClient commentFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private LikeFeignClient likeFeignClient;

    @Resource(name = "asyncServiceExecutor")
    private ThreadPoolTaskExecutor threadExecutor;

    private CountDownLatch countDownLatch; // 阻塞计数器


    public Boolean getIsLikeAsync(CountDownLatch countDownLatch, Long userId) {
        try {
            Future<Boolean> submit = threadExecutor.submit(() -> {
                Boolean isLike = likeFeignClient.getIsLike(userId);
                log.info(Thread.currentThread().getName() + "----------->获取是否点赞");
                return isLike;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("查询用户是否已点赞该视频失败---------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return false;
    }

    public UserVo getAuthorInfoAsync(CountDownLatch countDownLatch, Long authorId) {
        try {
            Future<UserVo> submit = threadExecutor.submit(() -> {
                UserVo authorInfo = userFeignClient.getUserInfoFromUserModuleByNotToken(authorId.toString());
                log.info(Thread.currentThread().getName() + "----------->获取作者信息");
                return authorInfo;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("查询投稿视频的作者信息,作者id为" + authorId + "----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return null;
    }

    public Integer getLikeCountAsync(CountDownLatch countDownLatch, Long videoId) {
        try {
            Future<Integer> submit = threadExecutor.submit(() -> {
                Integer likeCount = likeFeignClient.getLikeCount(videoId);
                log.info(Thread.currentThread().getName() + "----------->获取点赞数量");
                return likeCount;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("查询投稿视频的点赞数量失败----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return 0;
    }

    public Integer getCommnetNum(CountDownLatch countDownLatch, Long videoId) {
        try {
            Future<Integer> submit = threadExecutor.submit(() -> {
                Integer likeCount = likeFeignClient.getLikeCount(videoId);
                log.info(Thread.currentThread().getName() + "----------->获取点赞数量");
                return likeCount;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("查询投稿视频的点赞数量失败----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return 0;
    }
}
