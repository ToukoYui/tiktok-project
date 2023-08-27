package com.tiktok.service_comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.tiktok")//为了能够扫描含有@FeginCient注解的类
@SpringBootApplication
public class ServiceCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCommentApplication.class, args);
    }
}
