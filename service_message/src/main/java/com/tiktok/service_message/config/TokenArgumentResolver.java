package com.tiktok.service_message.config;

import com.tiktok.common_util.constant.ParamConstant;
import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.anno.OptionalParamAnno;
import com.tiktok.model.anno.TokenAuthAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


/**
 * 自定义参数解析器
 * 用于统一解析所有请求的携带的Token合法性
 */
@Component
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(TokenAuthAnno.class) || methodParameter.hasParameterAnnotation(OptionalParamAnno.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        try {
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            log.info("前端请求路径--------->" + request.getRequestURL());
            // 拿到请求的token参数
            String token = request.getParameter(ParamConstant.TOKEN);
            log.info("前端请求携带的Token---------->" + token);
            Long userIdByJjwt = JjwtUtil.getUserId(token);
            log.info("解析前端Token获取到的用户ID----------->" + userIdByJjwt);
            return userIdByJjwt;
        } catch (Exception e) {
            log.error("Token解析异常----------->" + e.getMessage());
            return null;
        }
    }
}
