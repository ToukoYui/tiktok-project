package com.tiktok.common_util.utils;

import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class JjwtUtil {
    private static String tokenSignKey = "123456";

    public static String createToken(Long userId, String userName) {
        long tokenExpiration = 24 * 3600 * 1000; //过期时间设为24小时
        String token = Jwts.builder()
                //设置签名（由自定义的密钥加密生成，前一个参数为加密密钥的算法）
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP) //设置jwt的类型
                .setSubject("KITKOT-USER")   //设置面向的用户
                //设置过期时间,参数为一个Date对象
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)  //用户相关信息的声明
                .claim("userName", userName)
                .compact(); //开始压缩为xxxxx.yyyyy.zzzzz 格式的jwt token
        return token;
    }


    // 根据token 获取userId
    // 登陆过期验证
    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;
        // 非空则继续
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }

}
