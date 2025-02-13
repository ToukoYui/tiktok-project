package com.tiktok.service_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.tiktok")//为了能够扫描含有@FeginCient注解的类
@SpringBootApplication
//@MapperScan("com.tiktok.service_user.mapper")
public class ServiceUserApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceUserApplication.class, args);
        GptUserCreSevice myAsyncService = context.getBean(GptUserCreSevice.class);
        myAsyncService.asyncMethod();
    }

}
