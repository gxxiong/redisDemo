package com.xgx.websocket;

import com.xgx.pojo.WebsocketMsg;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DependsOn("SpringContextUtils")
@ServerEndpoint(path = "/ws", port = "8091")
public class WebSocketServer {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebSocketServer.class);

    private String userId;

    @Autowired
    private WebsocketPublish websocketPublish;

    public static Map<String, List<Session>> socketMap = new HashMap<String, List<Session>>(64);


    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        logger.info("websocket handshake!");
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String id) {
        logger.info("WebSocketServer has a new connection");
        userId = id;
        logger.info("userId:" + userId);
        List<Session> sessionList = socketMap.get(userId);
        if (CollectionUtils.isEmpty(sessionList)) {
            sessionList = new ArrayList<Session>();
        }
        sessionList.add(session);
        socketMap.put(userId, sessionList);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        List<Session> sessionList = socketMap.get(userId);
        sessionList.remove(session);
        socketMap.put(userId, sessionList);
        logger.info("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        List<Session> sessionList = socketMap.get(userId);
        sessionList.remove(session);
        socketMap.put(userId, sessionList);
        logger.error("connection onError");
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("java websocket 收到消息==" + message);
        System.out.println(message);
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }


    public void sendMessageToUser(WebsocketMsg websocketMsg) {
        List<String> userIds = websocketMsg.getUserIds();
        if (CollectionUtils.isNotEmpty(userIds)) {
            //将剩下没有发送的消息推送到其他机器
            websocketMsg.setUserIds(userIds);
            websocketPublish.publish("xgx", websocketMsg);
        }
    }

}
