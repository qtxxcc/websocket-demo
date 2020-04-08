package com.example.websocket.demo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint(value = "/testWebSocket/{id}")
public class WebSocketProcess {

    /*
     * 持有每个webSocket对象，以key-value存储到线程安全ConcurrentHashMap，
     */
    private static ConcurrentHashMap<Long, WebSocketProcess> concurrentHashMap = new ConcurrentHashMap<>(12);

    /**
     * 会话对象
     **/
    private Session session;


    /*
     * 客户端创建连接时触发
     * */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") long id) {
        //每新建立一个连接，就把当前客户id为key,this为value存储到map中
        this.session = session;
        concurrentHashMap.put(id, this);
        log.info("Open a websocket. id={}", id);
    }

    /**
     * 客户端连接关闭时触发
     **/
    @OnClose
    public void onClose(Session session, @PathParam("id") long id) {
        //客户端连接关闭时，移除map中存储的键值对
        concurrentHashMap.remove(id);
        log.info("close a websocket, concurrentHashMap remove sessionId= {}", id);
    }

    /**
     * 接收到客户端消息时触发
     */
    @OnMessage
    public void onMessage(String message, @PathParam("id") String id) {
        log.info("收到客户端消息 id={},msg={}", id, message);
    }

    /**
     * 连接发生异常时候触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. ", error);
    }

    /**
     * 发送消息到指定客户端
     *
     * @param id
     * @param message
     */
    public void sendMessage(long id, String message) throws Exception {
        //根据id,从map中获取存储的webSocket对象
        WebSocketProcess webSocketProcess = concurrentHashMap.get(id);
        if (!ObjectUtils.isEmpty(webSocketProcess)) {
            //当客户端是Open状态时，才能发送消息
            if (webSocketProcess.session.isOpen()) {
                webSocketProcess.session.getBasicRemote().sendText(message);
            } else {
                log.error("websocket session={} is closed ", id);
            }
        } else {
            log.error("websocket session={} is not exit ", id);
        }
    }

    /**
     * 发送消息到所有客户端
     */
    public void sendAllMessage(String msg) throws Exception {
        log.info("online client count={}", concurrentHashMap.size());
        Set<Map.Entry<Long, WebSocketProcess>> entries = concurrentHashMap.entrySet();
        for (Map.Entry<Long, WebSocketProcess> entry : entries) {
            Long cid = entry.getKey();
            WebSocketProcess webSocketProcess = entry.getValue();
            boolean sessionOpen = webSocketProcess.session.isOpen();
            if (sessionOpen) {
                webSocketProcess.session.getBasicRemote().sendText(msg);
            } else {
                log.info("cid={} is closed,ignore send text", cid);
            }
        }
    }

}