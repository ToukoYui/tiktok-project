package com.tiktok.service_chat.websocket;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiktok.model.entity.user.User;
import com.tiktok.service_chat.config.WebSocketConfig;
import com.tiktok.model.entity.chat.ChatMessage;
import com.tiktok.service_chat.service.ChatService;
import com.tiktok.service_chat.service.chat_user.ChatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
@Slf4j
@ServerEndpoint(value = "/websocket/{username}",configurator= WebSocketConfig.class)
@Component
public class MyWebSocket {

    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String username;
    //获取全局容器
    //获取全局容器
    private ApplicationContext applicationContext;
    //聊天逻辑层service
    private ChatService chatService;




    /**
     * 连接建立成功调用的方法，初始化昵称、session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username,EndpointConfig config) {
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(httpSession.getServletContext());
        sessionMap.put(username, session);
        this.applicationContext = applicationContext;
        this.session = session;
        this.username = username;
        this.chatService = (ChatService) applicationContext.getBean("chatService");
        log.info("有新用户加入，username={}, 当前在线人数为：{}", username, sessionMap.size());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("users", array);
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("username", key);
            // {"username", "zhang", "username": "admin"}
            array.add(jsonObject);
        }
        sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
        webSocketSet.add(this);     //加入set中
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {

        //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为chatMsg对象
        log.info("服务端收到用户username={}的消息:{}", username, message);
        JSONObject obj = JSONUtil.parseObj(message);
        String toUser = obj.getStr("to"); // to表示发送给哪个用户，比如 admin
        String content = obj.getStr("content");
        Session toSession = sessionMap.get(toUser); // 根据 to用户名来获取 session，再通过session发送消息文本
        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", username);  // from 是 zhang
            jsonObject.set("content", content);  // content content
            this.sendMessage(jsonObject.toString(), toSession);
            log.info("发送给用户username={}，消息：{}", toUser, jsonObject.toString());
        } else {
            log.info("发送失败，未找到用户username={}的session", toUser);
        }
        try {
            //对chatMsg进行装箱
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setFromUser(username);
            chatMessage.setToUser(toUser);
            chatMessage.setSendTime(new Date());
            chatMessage.setContent(content);
            //保存聊天记录信息
            chatService.saveMessage(chatMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getAsyncRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
