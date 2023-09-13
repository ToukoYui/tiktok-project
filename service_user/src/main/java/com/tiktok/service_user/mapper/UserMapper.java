package com.tiktok.service_user.mapper;


import com.tiktok.model.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectByUserName(@Param("username") String userName);

    User selectByUserId(@Param("id") String id);

    User selectByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);

    void insert(User user);

    List<User> getUserInfoListByIdList(List<Long> userIdList);
}
