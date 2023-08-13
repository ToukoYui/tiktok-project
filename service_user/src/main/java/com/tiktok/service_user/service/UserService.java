package com.tiktok.service_user.service;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.service_user.mapper.UserMapper;
import com.tiktok.service_user.model.entity.User;
import com.tiktok.service_user.model.vo.UserLoginResp;
import com.tiktok.service_user.model.vo.UserRegisterResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册功能
     * @param username
     * @param password
     * @return
     */
    public UserRegisterResp userRegister(String username,String password) {
        UserRegisterResp userRegisterResp = new UserRegisterResp();
        // 输入信息非空则进行注册
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            // 查询用户
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user == null) {
                // 用户不存在则注册用户
                user = createUserByInfo(username, password);
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userRegisterResp.setUserId(user.getId());
                userRegisterResp.setStatusCode("200");
                userRegisterResp.setStatusMsg("用户注册成功");
                userRegisterResp.setToken(token);

                // 将token存入redis中
                redisTemplate.opsForValue().set("user:token:" + token,user.getId().toString(),2, TimeUnit.HOURS);
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
     * 还需要一个过滤/拦截器来验证token
     * 密码加密
     * @param username
     * @param password
     * @return
     */
    public UserLoginResp userLogin(String username, String password) {
        UserLoginResp userLoginResp = new UserLoginResp();


        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            userLoginResp.setStatusMsg("用户信息填写有误");
            userLoginResp.setStatusCode("401");
        } else {
            // 查询用户
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user != null) {
                // 查找到用户则生成一个token并返回给客户端
                userLoginResp.setUserId(user.getId());
                userLoginResp.setStatusMsg("登录成功");
                userLoginResp.setStatusCode("200");
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userLoginResp.setToken(token);

                // 将token存入redis中
                redisTemplate.opsForValue().set("user:token:" + token,user.getId().toString(),2, TimeUnit.HOURS);
            } else {
                userLoginResp.setStatusMsg("未找到用户");
                userLoginResp.setStatusCode("401");
            }
        }
        return userLoginResp;
    }
}
