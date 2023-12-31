package com.tiktok.service_message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import com.tiktok.model.entity.message.Message;
import com.tiktok.service_message.mapper.MessageMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import static com.theokanning.openai.service.OpenAiService.*;

@Service
public class ChatgptService {
    @Value("${chatgpt.apikey}")
    private String apiKey; // 替换为你的OpenAI API密钥
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Async
    public void send(Long userId,String content) {
        List<ChatMessage> chatMessageList  = buildChatMessage(content);
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        OkHttpClient client = defaultClient(apiKey,Duration.ofMinutes(1))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        OpenAiService service = new OpenAiService(api);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessageList)
                .model("gpt-3.5-turbo")
                .build();
        List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();
        String reply = choices.get(0).getMessage().getContent();
//        回复消息存入数据库
        Message message = new Message();
        message.setContent(reply);
        message.setToUserId(userId);
        message.setUserId(7l);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insertMessage(message);
        redisTemplate.delete(userId+"DontRepeatSend");
    }
    private List<ChatMessage> buildChatMessage(String message) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent(message);
        chatMessageList.add(chatMessage);
        return chatMessageList;
    }
}
