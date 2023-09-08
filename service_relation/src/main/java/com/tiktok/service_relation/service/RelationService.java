package com.tiktok.service_relation.service;

import cn.hutool.core.bean.BeanUtil;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.vo.TokenAuthSuccess;
import com.tiktok.model.vo.relation.MutualFollowResp;
import com.tiktok.model.vo.relation.RelationResp;
import com.tiktok.model.vo.user.FriendUser;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.service_relation.mapper.RelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RelationService {

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Resource
    private RedisTemplate<String, List<UserVo>> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> FOLLOW_SCRIPT;

    private static final DefaultRedisScript<Long> UNFOLLOW_SCRIPT;

    static {
        FOLLOW_SCRIPT = new DefaultRedisScript<>();
        FOLLOW_SCRIPT.setLocation(new ClassPathResource("follow.lua"));
        FOLLOW_SCRIPT.setResultType(Long.class);
    }

    static {
        UNFOLLOW_SCRIPT = new DefaultRedisScript<>();
        UNFOLLOW_SCRIPT.setLocation(new ClassPathResource("unfollow.lua"));
        UNFOLLOW_SCRIPT.setResultType(Long.class);
    }

    public RelationResp related(Long toUserId, String actionType, TokenAuthSuccess tokenAuthSuccess) {
        try {
            String userId = tokenAuthSuccess.getUserId();
            // 当前用户的关注者id列表
            String followUserIdKey = "followUserIds:" + userId;
            // 当前用户的粉丝id列表
            String followerIdKey = "followerIds:" + toUserId;
            // 查询用户是否关注过该用户
            Integer count = relationMapper.selectIsRelated(Long.valueOf(userId), toUserId);
            if (count == 0) {
                // 未关注该用户,添加新的关注
                relationMapper.insertRelate(Long.valueOf(userId), toUserId, 1);
                // 执行lua脚本,保证原子性操作
                Long r = stringRedisTemplate.execute(
                        FOLLOW_SCRIPT,
                        Collections.emptyList(),
                        userId, toUserId.toString()
                );
                if (r.intValue() != 0) {
                    // 手动回滚
                    relationMapper.updateRelated(Long.valueOf(userId), toUserId, 0);
                    return new RelationResp("500", "服务器异常哦~", null);
                }
//                stringRedisTemplate.opsForSet().add(followUserIdKey, toUserId.toString());
//                stringRedisTemplate.opsForSet().add(followerIdKey, userId);
            } else {
                // 已关注,修改是否关注即可
                if (actionType.equals("1")) {
                    relationMapper.updateRelated(Long.valueOf(userId), toUserId, 1);
                    // 将该用户id存入redis中
                    // set中值是不能重复的,如果添加相同元素,会添加失败
                    Long r = stringRedisTemplate.execute(
                            FOLLOW_SCRIPT,
                            Collections.emptyList(),
                            userId, toUserId.toString()
                    );
                    if (r.intValue() != 0) {
                        // 手动回滚
                        relationMapper.updateRelated(Long.valueOf(userId), toUserId, 0);
                        return new RelationResp("500", "服务器异常哦~", null);
                    }
//                    stringRedisTemplate.opsForSet().add(followUserIdKey, toUserId.toString());
//                    stringRedisTemplate.opsForSet().add(followerIdKey, userId);
                } else {
                    relationMapper.updateRelated(Long.valueOf(userId), toUserId, 0);
                    // 从redis中删除
                    Long r = stringRedisTemplate.execute(
                            UNFOLLOW_SCRIPT,
                            Collections.emptyList(),
                            userId, toUserId.toString()
                    );
                    if (r.intValue() != 0) {
                        // 手动回滚
                        relationMapper.updateRelated(Long.valueOf(userId), toUserId, 1);
                        return new RelationResp("500", "服务器异常哦~", null);
                    }
//                    stringRedisTemplate.opsForSet().remove(followUserIdKey, toUserId.toString());
//                    stringRedisTemplate.opsForSet().remove(followerIdKey, userId);
                }
            }
            // 关注后删除该用户缓存
            redisTemplate.delete("user:" + userId);
            redisTemplate.delete("followUser:" + userId);
            redisTemplate.delete("follower:" + toUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RelationResp("500", "服务器出错,关注失败", null);
        }
        // todo 仔细思考是否正确 因为这个缓存是我们没有的
//        redisTemplate.delete("userlist:public");
        return new RelationResp("0", actionType.equals("1") ? "关注成功" : "取消关注成功", null);
    }


    public RelationResp getRelatedUserList(Long userId) {
        // 查询缓存中是否存在
        String key = "followUser:" + userId;
        String idKey = "followUserIds:" + userId;
        if (redisTemplate.hasKey(key)) {
            List<UserVo> userVoList = redisTemplate.opsForValue().get(key);
            log.info("获取关注用户列表------->从缓存中获取");
            return new RelationResp("0", "获取关注用户列表成功", userVoList);
        }
        // 缓存中没有,从数据库中获取
        // 先获取id集合
        List<Long> userIdList = stringRedisTemplate.opsForSet().members(idKey).stream().map(
                Long::valueOf
        ).collect(Collectors.toList());

//        List<Integer> followUserIds = relationMapper.getFollowUserIds(userId);

        // 根据用户id列表获取关注用户详情列表
        List<UserVo> userInfoList = userFeignClient.getUserInfoList(userIdList);
        // 存入redis中
        redisTemplate.opsForValue().set(key, userInfoList, 2, TimeUnit.HOURS);
        log.info("获取关注用户列表------------->从MySQL中查询");
        return new RelationResp("0", "获取关注用户列表成功", userInfoList);
    }


    public RelationResp getFollowerList(Long userId) {
        // 查询缓存中是否存在
        String key = "follower:" + userId;
        String idKey = "followerIds:" + userId;
        if (redisTemplate.hasKey(key)) {
            List<UserVo> userVoList = redisTemplate.opsForValue().get(key);
            log.info("获取关注用户列表------->从缓存中获取");
            return new RelationResp("0", "获取粉丝用户列表成功", userVoList);
        }
        // 缓存中没有,从数据库中获取
//        List<Integer> followerIds = relationMapper.getFollowerIds(userId);
        // id列表从redis中获取
        List<Long> userIdList = stringRedisTemplate.opsForSet().members(idKey).stream().map(
                Long::valueOf
        ).collect(Collectors.toList());
        // 根据用户id列表获取粉丝用户详情列表
        List<UserVo> userInfoList = userFeignClient.getUserInfoList(userIdList);
        // 存入redis中
        redisTemplate.opsForValue().set(key, userInfoList, 2, TimeUnit.HOURS);
        log.info("获取粉丝用户列表------------->从MySQL中查询");
        return new RelationResp("0", "获取粉丝用户列表成功", userInfoList);
    }


    // todo 需要按照消息的发送时间先后对返回的列表进行排序
    // todo 需要message模块的相关功能
    public MutualFollowResp getFriendUserList(Long userId) {
        //  根据key获取共同关注用户的id集合
        String key = "followUserIds:" + userId;
        String key2 = "followerIds:" + userId;
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key, key2);
        // 判断是否有共同关注用户对象,没有则返回空集合
        if (intersect == null || intersect.isEmpty()) {
            return new MutualFollowResp("0", "没有互相关注的人哦~", null);
        }
        List<Long> userIdList = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserVo> userInfoList = userFeignClient.getUserInfoList(userIdList);
        List<FriendUser> friendUserList = userInfoList.stream().map(
                userVo -> {
                    FriendUser friendUser = new FriendUser();
                    BeanUtil.copyProperties(userVo, friendUser);
                    // todo 获取该好友的最新聊天信息

                    // todo message消息的类型
                    return friendUser;
                }
        ).collect(Collectors.toList());
        return new MutualFollowResp("0", "获取互相关注用户列表", friendUserList);
    }
}
