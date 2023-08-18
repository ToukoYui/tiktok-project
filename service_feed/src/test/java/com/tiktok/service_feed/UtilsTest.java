package com.tiktok.service_feed;

import com.tiktok.common_util.utils.JjwtUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    void encrypt(){
        StandardPBEStringEncryptor basicTextEncryptor = new StandardPBEStringEncryptor();
        String env =System.getenv("DouyinSecretKey"); //读取环境变量得到密钥
        basicTextEncryptor.setPassword(env); //设置密钥
        basicTextEncryptor.setAlgorithm("PBEWithMD5AndDES"); //加密算法
        String pwd = basicTextEncryptor.encrypt("PcgWBfvTovoUxHRZUEoFWfePUQBiz1qS"); // 加密“mysecret”文本
        System.out.println(pwd);
    }

    @org.junit.jupiter.api.Test
    public void getEnv(){
        String env =System.getenv("DouyinSecretKey");
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

}
