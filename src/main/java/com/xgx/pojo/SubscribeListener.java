package com.xgx.pojo;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.yeauty.pojo.Session;

public class SubscribeListener implements MessageListener {

    private StringRedisTemplate stringRedisTemplate;

    private Session session;

    /**
     * 订阅接收发布者的消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        System.out.println(new String(pattern) + "主题发布：" + msg);
        if (null != session) {
            session.sendText(msg);
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
