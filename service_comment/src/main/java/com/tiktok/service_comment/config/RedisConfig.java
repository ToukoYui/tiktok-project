package com.tiktok.service_comment.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.tiktok.model.vo.comment.CommentVo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis操作对象需要序列化
 * 这里用json格式的序列化（默认是jdk格式）
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Long> redisTemplateInteger(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, CommentVo> redisTemplateCommentVo(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, CommentVo> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器
        //使用Jackson 2，将对象序列化为JSON
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        template.setValueSerializer(fastJsonRedisSerializer);
        return template;
    }
}
