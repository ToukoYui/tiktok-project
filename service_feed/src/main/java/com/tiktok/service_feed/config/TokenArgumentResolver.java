package com.tiktok.service_feed.config;

import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.anno.TokenAuthAnno;
import com.tiktok.model.vo.TokenAuthSuccess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;


/**
 * 自定义参数解析器
 * 用于统一解析所有请求的携带的Token合法性
 */
@Component
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(TokenAuthAnno.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        try{
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            log.info("前端请求路径--------->" + request.getRequestURL());
            // 拿到请求的token参数
            String token = request.getParameter("token");
            log.info("前端请求携带的Token---------->" + token);
            String userIdByRedis = redisTemplate.opsForValue().get("user:token:" + token);
            log.info("Redis中通过Token获取到的用户ID----------->"+userIdByRedis);
            String userIdByJjwt = JjwtUtil.getUserId(token).toString();
            log.info("解析前端Token获取到的用户ID----------->"+userIdByJjwt);
            if (StringUtils.isEmpty(userIdByRedis) || !userIdByRedis.equals(userIdByJjwt)){
                log.error("Token解析异常----------->前端token与redis的token解析不一致，认证失败");
                return null;
            }
            log.info("token认证通过，允许后续请求处理");
            return new TokenAuthSuccess(userIdByRedis,token);
        }catch (Exception e){
            log.error("Token解析异常----------->" + e.getMessage());
        }
        return null;
    }
}
