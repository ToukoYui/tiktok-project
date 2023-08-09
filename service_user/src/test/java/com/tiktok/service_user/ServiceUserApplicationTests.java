package com.tiktok.service_user;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.service_user.mapper.UserMapper;
import com.tiktok.service_user.model.entity.User;
import com.tiktok.service_user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@MapperScan("com/tiktok/service_user/mapper")
class ServiceUserApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        int row = userMapper.insert(new User(1L, "awdwa", "dawawd"));
        System.out.println("row = " + row);
    }

    @Test
    void testToken() {
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ" +
                ".H4sIAAAAAAAAAKtWKi5NUrJS8vYM8fYP0Q0Ndg1S0lFKrShQsjI0szQ0tTQxMDfSUSotTi3yTFGyMoYw_RJzU4GacjKLM5VqAQWfcLtDAAAA" +
                ".E0INzm-VRGfydxBC1zsD8o04KivO6VUG6cTlxFssA5kiZveHziFCRd-9OjAxRtF7WowHXCrKfKl9JlXgnupmgw";
        Long userId = JjwtUtil.getUserId(token);
        System.out.println(userId);
    }
}
