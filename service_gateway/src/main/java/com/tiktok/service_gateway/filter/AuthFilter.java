package com.tiktok.service_gateway.filter;

import com.alibaba.fastjson.JSON;
import com.tiktok.common_util.utils.JjwtUtil;
import com.tiktok.model.vo.GatewayResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局Filter，统一处理登录与外部不允许访问的服务
 */
@Order
@Component
public class AuthFilter implements GlobalFilter{


    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private Pattern pattern;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String path = request.getURI().getPath();
        System.out.println("===" + path);
        for (String pattern : pattern.getWhitelist()){
            if(pattern.equals(path)){
                return chain.filter(exchange);
            }
        }
        String token = queryParams.getFirst("token");
        System.out.println(token);
        GatewayResp resp = new GatewayResp("401", "请先登录哦~");
        if (StringUtils.isEmpty(token)) {
            //响应中放入返回的状态吗, 没有权限访问
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer wrap = bufferFactory.wrap(JSON.toJSONBytes(resp));
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.fromSupplier(() -> wrap));
        }
        try {
            JjwtUtil.getUserId(token);
            return chain.filter(exchange);
        }catch (Exception e){
            //响应中放入返回的状态吗, 没有权限访问
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBufferFactory bufferFactory = response.bufferFactory();
            DataBuffer wrap = bufferFactory.wrap(JSON.toJSONBytes(resp));
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.fromSupplier(() -> wrap));
        }
    }
}
