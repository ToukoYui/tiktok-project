package com.tiktok.service_user.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.service_user.mapper.UserMapper;
import com.tiktok.model.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.tiktok.model.vo.user.UserLoginResp;
import com.tiktok.model.vo.user.UserRegisterResp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AsyncService asyncService;

    private CountDownLatch countDownLatch; // 阻塞计数器
    private static final String slat = "tiktok!@#";


    /**
     * 用户注册功能
     *
     * @param username
     * @param password
     * @return
     */
    public UserRegisterResp userRegister(String username, String password) {
        UserRegisterResp userRegisterResp = new UserRegisterResp();
        // 输入信息非空则进行注册
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            password = DigestUtils.md5Hex(password+slat);
            // 查询用户
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user.getId() == 7){
                return new UserLoginResp(444,"你登陆尼玛gpt账号呢？",null,null);
            }
            if (user == null) {
                // 用户不存在则注册用户
                user = createUserByInfo(username, password);
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userRegisterResp.setUserId(user.getId());
                userRegisterResp.setStatusCode("0");
                userRegisterResp.setStatusMsg("用户注册成功");
                userRegisterResp.setToken(token);
            } else {
                userRegisterResp.setStatusMsg("用户已存在,请勿重复注册");
                userRegisterResp.setStatusCode("401");
            }
        } else {
            userRegisterResp.setStatusMsg("用户信息填写有误");
            userRegisterResp.setStatusCode("401");
        }
        return userRegisterResp;
    }

    // 注册成功返回用户id
    private User createUserByInfo(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        // 将用户保存到数据库的同时，mybatis把自增id放进user对象中
        userMapper.insert(user);
        return user;
    }


    /**
     * 用户登录功能
     *
     * @param username
     * @param password
     * @return
     */
    public UserLoginResp userLogin(String username, String password) {
        UserLoginResp userLoginResp = new UserLoginResp();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            userLoginResp.setStatusMsg("用户信息填写有误");
            userLoginResp.setStatusCode(401);
        } else {
            // 查询用户
            password = DigestUtils.md5Hex(password+slat);
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user != null) {
                // 查找到用户则生成一个token并返回给客户端
                userLoginResp.setUserId(user.getId());
                userLoginResp.setStatusMsg("登录成功");
                userLoginResp.setStatusCode(0);
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userLoginResp.setToken(token);
            } else {
                userLoginResp.setStatusMsg("未找到用户");
                userLoginResp.setStatusCode(401);
            }
        }
        return userLoginResp;
    }


    /**
     * 获取用户信息功能
     * 可能会有Long类型转换为String类型的bug
     *
     * @param userId
     * @return
     */
    public UserVo getUserInfo(Long userId) {
        String key = "user:" + userId;
        // 先去redis中查询,查询不到再去数据库,并存入redis中
        String jsonObjectStr = redisTemplate.opsForValue().get(key);
        if (jsonObjectStr == null) {
            // 4个服务异步获取
            int cul = 4;
            // 如果获取不到则去数据库查询,并缓存到redis中
            String id = String.valueOf(userId);
            // 对id进行非空判断
            if (id != null && !id.equals("")) {
                User user = userMapper.selectByUserId(id);
                // 如果获取不到user,说明提供的userId有误
                if (user == null) {
                    // 返回一个空对象
                    return new UserVo();
                }
                log.info("获取用户信息，从MYSQL中获取------------->" + user.toString());

                try {
                    countDownLatch = new CountDownLatch(cul);
                    UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
                    // 异步获取各种信息
                    // thread1.获取当前用户关注总数
                    Integer followUserCountAsync = asyncService.getFollowUserCountAsync(countDownLatch, userId);

                    // thread2.获取当前用户的粉丝总数
                    Integer followerCountAsync = asyncService.getFollowerCountAsync(countDownLatch, userId);

                    // thread3.获取发布视频的数量
                    Integer videoNumByUserIdAsync = asyncService.getVideoNumByUserIdAsync(countDownLatch, userId);

                    // thread4.获取喜欢视频的数量
                    Integer likeCountByUserIdAsync = asyncService.getLikeCountByUserIdAsync(countDownLatch, userId);

                    //当所有线程执行完毕后才继续执行后续代码
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    userVo.setFollowCount(followUserCountAsync);
                    userVo.setFollowerCount(followerCountAsync);
                    userVo.setWorkCount(videoNumByUserIdAsync);
                    userVo.setFavoriteCount(likeCountByUserIdAsync);
                    // 设置关注情况,默认是false
                    userVo.setIsFollow(false);
                    jsonObjectStr = JSONObject.toJSONString(userVo);
                    redisTemplate.opsForValue().set(key, jsonObjectStr, 2, TimeUnit.HOURS);
                } catch (Exception e) {
                    log.error("发生异常,无法获取用户信息------------->" + e.getMessage());
                    return null;
                }
            }
        }
        // 转换为对象
        return JSONObject.parseObject(jsonObjectStr, UserVo.class);
    }



    /**
     * 根据用户id列表获取用户信息列表
     * 适用于获取关注列表和粉丝列表
     * @param userIdList
     * @return
     */
    public List<UserVo> getUserInfoList(List<Long> userIdList, Long userId) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return new ArrayList<UserVo>();
        }
        // 从数据库中获取
        // 最多5个服务异步获取
        int cul;
        boolean flag;
        if (userId != null) {
            flag = true;
            cul = 5;
        } else {
            flag = false;
            cul = 4;
        }
        try {
            List<User> userList = userMapper.getUserInfoListByIdList(userIdList);
            List<UserVo> userVoList = userList.stream().map(
                    user -> {
                        // 开启线程池
                        countDownLatch = new CountDownLatch(cul);

                        UserVo userVo = new UserVo();
                        BeanUtil.copyProperties(user, userVo);

                        // 异步获取各种信息
                        // thread1.获取当前用户关注总数
                        Integer followUserCountAsync = asyncService.getFollowUserCountAsync(countDownLatch, userId);

                        // thread2.获取当前用户的粉丝总数
                        Integer followerCountAsync = asyncService.getFollowerCountAsync(countDownLatch, userId);

                        // thread3.获取发布视频的数量
                        Integer videoNumByUserIdAsync = asyncService.getVideoNumByUserIdAsync(countDownLatch, userId);

                        // thread4.获取喜欢视频的数量
                        Integer likeCountByUserIdAsync = asyncService.getLikeCountByUserIdAsync(countDownLatch, userId);

                        // thread5.获取关注关系
                        Boolean isRelatedAsync = false;
                        if(flag){
                            // 登录用户才能获取关注关系
                            isRelatedAsync = asyncService.getIsRelatedAsync(countDownLatch, userVo.getId(), userId);
                        }

                        //当所有线程执行完毕后才继续执行后续代码
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                        userVo.setFollowCount(followUserCountAsync);
                        userVo.setFollowerCount(followerCountAsync);
                        userVo.setWorkCount(videoNumByUserIdAsync);
                        userVo.setFavoriteCount(likeCountByUserIdAsync);
                        userVo.setIsFollow(isRelatedAsync);
                        return userVo;
                    }
            ).collect(Collectors.toList());
            return userVoList;
        } catch (Exception e) {
            log.error("发生异常,无法获取用户信息------------->" + e.getMessage());
            return null;
        }
    }

}
