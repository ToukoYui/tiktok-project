package com.tiktok.service_user.service;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.service_user.mapper.UserMapper;
import com.tiktok.service_user.model.entity.User;
import com.tiktok.service_user.model.vo.UserRegisterReq;
import com.tiktok.service_user.model.vo.UserRegisterResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册功能
     *
     * @param registerReq
     * @return
     */
    public UserRegisterResp userRegister(UserRegisterReq registerReq) {
        UserRegisterResp userRegisterResp = new UserRegisterResp();
        if (!(registerReq == null || registerReq.getUsername() == null || registerReq.getPassword() == null)) {
            // 输入信息非空则进行注册
            String username = registerReq.getUsername();
            String password = registerReq.getPassword();
            // 查询用户
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user == null) {
                // 用户不存在则注册用户
                createUserByInfo(username, password);
                userRegisterResp.setStatusCode("200");
                userRegisterResp.setStatusMsg("用户注册成功");
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


    private User createUserByInfo(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        // 将用户保存到数据库
        userMapper.insert(user);
        return user;
    }


    /**
     * 用户登录功能
     * 还需要一个过滤/拦截器来验证token
     * 密码加密
     * @param registerReq
     */
    public UserRegisterResp userLogin(UserRegisterReq registerReq) {
        UserRegisterResp userRegisterResp = new UserRegisterResp();
        if (registerReq == null || registerReq.getUsername() == null || registerReq.getPassword() == null) {
            userRegisterResp.setStatusMsg("用户信息填写有误");
            userRegisterResp.setStatusCode("401");
        } else {
            String username = registerReq.getUsername();
            String password = registerReq.getPassword();
            // 查询用户
            User user = userMapper.selectByUserNameAndPassword(username, password);
            if (user != null) {
                // 查找到用户则生成一个token并返回给客户端
                userRegisterResp.setUserId(user.getId());
                userRegisterResp.setStatusMsg("登录成功");
                userRegisterResp.setStatusCode("200");
                // 调用接口生成并返回token
                String token = JjwtUtil.createToken(user.getId(), user.getUsername());
                userRegisterResp.setToken(token);

            } else {
                userRegisterResp.setStatusMsg("未找到用户");
                userRegisterResp.setStatusCode("401");
            }
        }
        return userRegisterResp;
    }


}
