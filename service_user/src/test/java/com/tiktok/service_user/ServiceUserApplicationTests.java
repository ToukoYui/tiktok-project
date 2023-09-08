package com.tiktok.service_user;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.feign_util.utils.UserFeignClient;
import com.tiktok.model.vo.user.UserVo;
import com.tiktok.service_user.mapper.UserMapper;
import com.tiktok.service_user.service.UserService;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@MapperScan("com/tiktok/service_user/mapper")
class ServiceUserApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Test
    void encrypt() {
        StandardPBEStringEncryptor basicTextEncryptor = new StandardPBEStringEncryptor();
        String env = System.getenv("DouyinSecretKey"); //读取环境变量得到密钥
        basicTextEncryptor.setPassword(env); //设置密钥
        basicTextEncryptor.setAlgorithm("PBEWithMD5AndDES"); //加密算法
        String pwd = basicTextEncryptor.encrypt("mysecret"); // 加密“mysecret”文本
        System.out.println(pwd);
    }

    @Test
    public void getEnv() {
        String env = System.getenv("DouyinSecretKey");
        System.out.println("env = " + env);
    }

    @Test
    void testToken() {
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ" +
                ".H4sIAAAAAAAAAKtWKi5NUrJS8vYM8fYP0Q0Ndg1S0lFKrShQsjI0szQ0tTQxMDfSUSotTi3yTFGyMoYw_RJzU4GacjKLM5VqAQWfcLtDAAAA" +
                ".E0INzm-VRGfydxBC1zsD8o04KivO6VUG6cTlxFssA5kiZveHziFCRd-9OjAxRtF7WowHXCrKfKl9JlXgnupmgw";
        Long userId = JjwtUtil.getUserId(token);
        System.out.println(userId);
    }

    @Test
    void testGetInfoWithToken() {

    }

    @Test
    void testGetUserListFromIds() {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(5L);
        longs.add(2L);
        List<UserVo> userInfoList = userService.getUserInfoList(longs);
        System.out.println(userInfoList);
    }
}
