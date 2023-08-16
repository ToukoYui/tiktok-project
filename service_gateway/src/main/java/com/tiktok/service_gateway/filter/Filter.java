package com.tiktok.service_gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.vo.GatewayResp;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局Filter，统一处理登录与外部不允许访问的服务
 */
@Order
@Component
public class Filter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("===" + path);

//        //内部服务接口，不允许外部访问
//        if(antPathMatcher.match("/**/inner/**", path)) {
//            ServerHttpResponse response = exchange.getResponse();
//            return out(response, ResultCodeEnum.PERMISSION);
//        }


        //api接口，异步请求，校验用户必须登录
//        if (antPathMatcher.match("/douyin/**", path)) {
//            //从请求头的token中获取userId
//            Long userId = this.getUserId(request);
//            System.out.println("userId = " + userId);
//            if (StringUtils.isEmpty(userId)) {
//                ServerHttpResponse response = exchange.getResponse();
//                return out(response);
//            }
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * api接口鉴权失败返回数据
     *
     * @param response
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response) {
        GatewayResp result = new GatewayResp("208", "未登录");
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 获取当前登录用户id
     *
     * @param request
     * @return
     */
    private Long getUserId(ServerHttpRequest request) {
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if (null != tokenList) {
            token = tokenList.get(0);
        }
        if (!StringUtils.isEmpty(token)) {
            return JjwtUtil.getUserId(token);
        }
        return null;
    }
}
