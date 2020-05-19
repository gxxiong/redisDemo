package com.xgx.config;

import com.xgx.pojo.PublishService;
import com.xgx.pojo.SubscribeListener;
import com.xgx.util.SpringContextUtils;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@DependsOn("SpringContextUtils")
@ServerEndpoint(path = "/ws",port = "8091")
public class MyWebSocket {

    private StringRedisTemplate redisTampate = SpringContextUtils.getBean(StringRedisTemplate.class);

    private RedisMessageListenerContainer redisMessageListenerContainer = SpringContextUtils.getBean(RedisMessageListenerContainer.class);

    //存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
    private static CopyOnWriteArraySet<Session> sessionList = new CopyOnWriteArraySet<>();

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
//        session.setSubprotocols("stomp");
        if (!req.equals("ok")) {
            System.out.println("Authentication failed!");
//            session.close();
        }
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        sessionList.add(session);

        SubscribeListener subscribeListener = new SubscribeListener();
        subscribeListener.setSession(session);
        subscribeListener.setStringRedisTemplate(redisTampate);
        //设置订阅topic
        redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic("xgx"));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessionList.remove(session);
        System.out.println("one connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("java websocket 收到消息=="+message);
        PublishService publishService = SpringContextUtils.getBean(PublishService.class);
//        publishService.publish("1", message);

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
}
