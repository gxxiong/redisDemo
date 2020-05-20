package com.xgx.websocket;

import com.xgx.util.SpringContextUtils;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@DependsOn("SpringContextUtils")
@ServerEndpoint(path = "/ws", port = "8091")
public class WebSocketServer {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebSocketServer.class);

    private String userId;

    private RedisMessageListenerContainer redisMessageListenerContainer = SpringContextUtils.getBean(RedisMessageListenerContainer.class);

    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

//    private RedisMessageListener redisMessageListener = new RedisMessageListener();

    public static Map<String, Session> socketMap = new HashMap<String, Session>();

    //存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
    private static CopyOnWriteArraySet<Session> sessionList = new CopyOnWriteArraySet<>();


    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
//        session.setSubprotocols("stomp");
        System.out.println("websocket handshake!");
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String id) {
        userId = id;
        sessionList.add(session);
        socketMap.put(userId, session);

//        redisMessageListener.setSession(session);
//        redisMessageListener.setGenericJackson2JsonRedisSerializer(genericJackson2JsonRedisSerializer);
//        //设置订阅topic
//        redisMessageListenerContainer.addMessageListener(redisMessageListener, new ChannelTopic("xgx"));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessionList.remove(session);
        socketMap.remove(userId);
//        redisMessageListenerContainer.removeMessageListener(redisMessageListener);
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("java websocket 收到消息==" + message);
        System.out.println(message);
//        session.sendText("Hello Netty!");
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


    public boolean sendMessageToUser(String clientId, String message) {
        Session session = socketMap.get(clientId);
        if (session == null) {
            return false;
        }
        logger.info("进入发送消息");
        if (!session.isOpen()) {
            return false;
        }
        try {
            logger.info("正在发送消息");
            session.sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
