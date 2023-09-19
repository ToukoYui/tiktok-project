package com.tiktok.service_user.service;

import com.tiktok.feign_util.utils.LikeFeignClient;
import com.tiktok.feign_util.utils.RelationFeignClient;
import com.tiktok.feign_util.utils.VideoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Service
@Slf4j
public class AsyncService {

    @Autowired
    private VideoFeignClient videoFeignClient;
    @Autowired
    private LikeFeignClient likeFeignClient;
    @Autowired
    private RelationFeignClient relationFeignClient;


    @Resource(name = "asyncServiceExecutor")
    private ThreadPoolTaskExecutor threadExecutor;

    private CountDownLatch countDownLatch; // 阻塞计数器

    public Integer getFollowUserCountAsync(CountDownLatch countDownLatch, Long userId) {
        try {
            Future<Integer> submit = threadExecutor.submit(() -> {
                Integer followUserCount = relationFeignClient.getFollowUserCount(userId);
                log.info(Thread.currentThread().getName() + "----------->获取关注数量:" + followUserCount);
                return followUserCount;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("获取用户的关注数量失败----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return 0;
    }

    public Integer getFollowerCountAsync(CountDownLatch countDownLatch, Long userId) {
        try {
            Future<Integer> submit = threadExecutor.submit(() -> {
                Integer followerCount = relationFeignClient.getFollowerCount(userId);
                log.info(Thread.currentThread().getName() + "----------->获取粉丝数量:" + followerCount);
                return followerCount;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("获取用户的粉丝数量失败----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return 0;
    }


    public Integer getVideoNumByUserIdAsync(CountDownLatch countDownLatch, Long userId) {
        try {
            Future<Integer> submit = threadExecutor.submit(() -> {
                Integer videoNum = videoFeignClient.getVideoNumByUserId(userId);
                log.info(Thread.currentThread().getName() + "----------->获取发布视频的数量:" + videoNum);
                return videoNum;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("获取发布视频的数量失败----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return 0;
    }


    public Integer getLikeCountByUserIdAsync(CountDownLatch countDownLatch, Long userId) {
        try {
            Future<Integer> submit = threadExecutor.submit(() -> {
                Integer likedVideoNum = likeFeignClient.getLikeCountByUserId(userId);
                log.info(Thread.currentThread().getName() + "----------->获取喜欢视频的数量:" + likedVideoNum);
                return likedVideoNum;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("获取喜欢视频的数量失败----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return 0;
    }

    public Boolean getIsRelatedAsync(CountDownLatch countDownLatch, Long authorId, Long userId) {
        try {
            Future<Boolean> submit = threadExecutor.submit(() -> {
                boolean isRelated = relationFeignClient.getIsRelated(authorId, userId);
                log.info(Thread.currentThread().getName() + "----------->获取关注关系:" + isRelated);
                return isRelated;
            });
            return submit.get();
        } catch (Exception e) {
            log.error("获取关注关系----------->" + e.getMessage());
        } finally {
            countDownLatch.countDown(); // 计数器减1
        }
        return false;
    }

}
