package com.tiktok.service_favorite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.tiktok")//为了能够扫描含有@FeginCient注解的类
@SpringBootApplication
public class ServiceFavoriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFavoriteApplication.class, args);
    }

}
