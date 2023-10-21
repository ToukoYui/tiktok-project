package com.tiktok.service_relation;

import com.tiktok.service_message.service.ChatgptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceMessageApplicationTests {

    @Autowired
    private ChatgptService chatgptService;

    @Test
    void contextLoads() {
        chatgptService.send(1L,"你好");
    }


}
