package com.tiktok.service_user.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.vo.user.UserResp;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.service_user.mapper.UserMapper;
import com.tiktok.model.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.tiktok.model.vo.user.UserLoginResp;
import com.tiktok.model.vo.user.UserRegisterResp;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

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
            // 查询用户
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user == null) {
                // 用户不存在则注册用户
                user = createUserByInfo(username, password);
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userRegisterResp.setUserId(user.getId());
                userRegisterResp.setStatusCode("0");
                userRegisterResp.setStatusMsg("用户注册成功");
                userRegisterResp.setToken(token);

                // 将token存入redis中
                redisTemplate.opsForValue().set("user:token:" + token, user.getId().toString(), 2, TimeUnit.HOURS);
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
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user != null) {
                // 查找到用户则生成一个token并返回给客户端
                userLoginResp.setUserId(user.getId());
                userLoginResp.setStatusMsg("登录成功");
                userLoginResp.setStatusCode(0);
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userLoginResp.setToken(token);

                // 将token存入redis中
                redisTemplate.opsForValue().set("user:token:" + token, user.getId().toString(), 2, TimeUnit.HOURS);
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
                // 存储到redis中
                // Long类型的数据转换为String类型可能会出现bug
                UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
                jsonObjectStr = JSONObject.toJSONString(userVo);
                redisTemplate.opsForValue().set(key, jsonObjectStr, 2, TimeUnit.HOURS);
            }
        }
        // 转换为对象
        return JSONObject.parseObject(jsonObjectStr, UserVo.class);
    }


    /**
     * 用户注销功能
     * @param nativeWebRequest
     * @return
     */
    public boolean userLogout(NativeWebRequest nativeWebRequest) {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getParameter("token");
        // 删除redis中的token
        return redisTemplate.delete("user:token:" + token);

    }


}
