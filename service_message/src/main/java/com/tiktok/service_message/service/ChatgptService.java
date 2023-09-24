package com.tiktok.service_message.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tiktok.model.entity.message.Message;
import com.tiktok.service_message.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatgptService {
    private static String apiKey = "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"; // 替换为你的OpenAI API密钥
    private static String endpoint = "https://api.openai.com/v1/chat/completions";
    private Map<Long, List<String>> userSessions = new HashMap<>();
    @Autowired
    private MessageMapper messageMapper;
    @Async
    public void send(Long userId,String content) {
        // 检查用户会话是否存在
        if (!userSessions.containsKey(userId)) {
            //第一次进来肯定不存在会话中，这时候我们放进去
            userSessions.put(userId, new ArrayList<>());
        }
        //把发送的消息扔进这个人的list中
        List<String> sessionMessages = userSessions.get(userId);
        sessionMessages.add(content);

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 构建请求体
        String requestBody = buildRequestBody(sessionMessages);

        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //我用的代理端口7890，改为你自己开代理用的端口号
        factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)));
        restTemplate.setRequestFactory(factory);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(endpoint,request, String.class);
        // 提取回复消息
        String responseBody = response.getBody();
        String reply = extractReplyFromResponse(responseBody);
        //回复消息存入数据库
        Message message = new Message();
        message.setContent(reply);
        message.setToUserId(userId);
        message.setUserId(7l);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insertMessage(message);
        //把回复消息也存进当前用户的的list中，方便上下文记忆
        sessionMessages.add(reply);
    }
    private String buildRequestBody(List<String> sessionMessages) {
        JSONArray messagesArray = new JSONArray();
        for (String message : sessionMessages) {
            JSONObject messageObj = new JSONObject();
            messageObj.put("role", "user");
            messageObj.put("content", message);
            messagesArray.add(messageObj);
        }
        JSONObject requestBodyObj = new JSONObject();
        requestBodyObj.put("model", "gpt-3.5-turbo");
        requestBodyObj.put("messages", messagesArray);

        return requestBodyObj.toString();
    }
    private String extractReplyFromResponse(String response) {
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        String reply = message.getStr("content");

        return reply;
    }
}

