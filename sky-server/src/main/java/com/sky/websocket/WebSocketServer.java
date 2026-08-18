package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {
    private static final Map<Long,Session>sessionMap=new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,@PathParam("sid") Long sid){
        sessionMap.put(sid, session);
        log.info("WebSocket 连接建立：sid={}，当前连接数：{}", sid, sessionMap.size());
    }

    @OnClose
    public void onClose (Session session,@PathParam("sid") Long sid){
        sessionMap.remove(sid);
        log.info("WebSocket 连接关闭：sid={}，当前连接数：{}", sid, sessionMap.size());
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.error("WebSocket 连接发生错误", error);
    }

    public static void sendToAll(String message){
        if(sessionMap.isEmpty()){
            log.info("没有 WebSocket 客户端连接，消息不推送：{}", message);
            return;
        }
        for(Map.Entry<Long,Session>entry:sessionMap.entrySet()){
            try{
                entry.getValue().getBasicRemote().sendText(message);
                log.info("消息推送成功：sid={}，消息={}", entry.getKey(), message);
            }catch(Exception e){
                log.error("消息推送失败：sid={}", entry.getKey(), e);
            }
        }
    }
}
