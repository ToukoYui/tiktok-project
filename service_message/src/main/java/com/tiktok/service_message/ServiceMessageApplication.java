package com.tiktok.service_message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.tiktok")//为了能够扫描含有@FeginCient注解的类
@SpringBootApplication
public class ServiceMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMessageApplication.class, args);
    }

}
