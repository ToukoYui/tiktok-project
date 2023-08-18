package com.tiktok.service_feed.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OssPropertiesUtils implements InitializingBean {

//    @Value("${tencentyun.oss.endpoint}")
//    private String endpoint;

    @Value("${tencentyun.oss.secretId}")
    private String secretId;

    @Value("${tencentyun.oss.secretKey}")
    private String secretKey;

    @Value("${tencentyun.oss.cosRegion}")
    private String cosRegion;

    @Value("${tencentyun.oss.bucket}")
    private String bucket;

    //    public static String EDNPOINT;
    public static String SECRECT_ID;
    public static String SECRECT_KEY;
    public static String COS_REGION;
    public static String BUCKET;

    @Override
    public void afterPropertiesSet() throws Exception {
//        EDNPOINT=endpoint;
        SECRECT_ID=secretId;
        SECRECT_KEY=secretKey;
        COS_REGION=cosRegion;
        BUCKET=bucket;
    }
}