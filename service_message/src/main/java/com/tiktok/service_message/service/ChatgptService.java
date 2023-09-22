package com.tiktok.service_message.service;

import cn.hutool.http.HttpUtil;
import com.tiktok.service_message.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ChatgptService {
    private static String apiKey = "sk-wvWM0xGcxFfsddfsgxixbXK5tHovM"; // 替换为你的OpenAI API密钥
    private static String endpoint = "https://api.openai.com/v1/chat/completions";
    @Autowired
    private MessageMapper messageMapper;
    public void send(String content) {
        // 构建要发送的请求内容
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \""+content+"\"}]}";
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey);
        String body =  HttpUtil.createPost(endpoint)
                .addHeaders(headers)
                .body(requestBody).execute().body();
        System.out.println(body);
        //todo 获取gpt的回复消息存入数据库
    }
}
