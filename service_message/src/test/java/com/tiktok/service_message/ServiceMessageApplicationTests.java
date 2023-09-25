package com.tiktok.service_message;

import com.tiktok.service_message.service.ChatgptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootTest
@EnableAsync
public class ServiceMessageApplicationTests {
    @Autowired
    private ChatgptService chatgptService;
    @Test
    void contextLoads() {

    }
    @Test
    void test(){
        chatgptService.send(6l,"你好");
    }
}
