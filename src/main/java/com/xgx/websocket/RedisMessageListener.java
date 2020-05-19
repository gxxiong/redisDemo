package com.xgx.websocket;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.yeauty.pojo.Session;


public class RedisMessageListener implements MessageListener {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageListener.class);

    private StringRedisTemplate stringRedisTemplate;

    private Session session;

    /**
     * 订阅接收发布者的消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());

//        Session session = MyWebSocket.socketMap.get("1");
        System.out.println(session);

        if (session != null && session.isOpen()) {
            try {
                session.sendText(msg);
            } catch (Exception e) {
                logger.error("RedisSubListener消息订阅监听异常：" + e.getMessage());
            }
        }
    }


    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
