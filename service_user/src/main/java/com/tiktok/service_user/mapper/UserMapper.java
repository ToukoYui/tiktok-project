package com.tiktok.service_user.mapper;

import com.tiktok.service_user.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectByUserNameAndPassword(@Param("username") String userName, @Param("password") String password);

    int insert(User user);

}
