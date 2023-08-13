package com.tiktok.service_user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenBacketLimiter {
    private static long capacity;
    private static long rate; // 每rate秒补充一个令牌
    private static long curTokenNum;
    private static long lastTime = System.currentTimeMillis();

    @Value("${tokenbucket.capacity}")
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
    @Value("${tokenbucket.rate}")
    public void setRate(int rate){
        this.rate = rate;
    }

    @Value("${tokenbucket.capacity}")
    public void setCurToken(int capacity){
        this.curTokenNum = capacity;
    }

    public static synchronized boolean getToken(){
        long nowTime = System.currentTimeMillis();
        if (curTokenNum >0 ){
            curTokenNum--;
            lastTime = nowTime;
            return true;
        }

        long durable = (nowTime-lastTime)/1000; //前后两次请求的间隔时间
        long newTokenNum = durable/rate;
        if (newTokenNum <1){
            lastTime = nowTime;
            return false;
        }
        curTokenNum += newTokenNum-1;
        if (curTokenNum > capacity){
            curTokenNum = capacity;
        }
        lastTime = nowTime;
        return true;
    }
}
