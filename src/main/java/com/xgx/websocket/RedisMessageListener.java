package com.xgx.websocket;

import com.xgx.pojo.MessageInfo;
import lombok.Data;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.yeauty.pojo.Session;

import java.util.ArrayList;
import java.util.List;


@Data
public class RedisMessageListener implements MessageListener {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageListener.class);

    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer;

    private Session session;

    /**
     * 订阅接收发布者的消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        MessageInfo messageInfo = (MessageInfo) genericJackson2JsonRedisSerializer.deserialize(message.getBody());
        List<String> userIds = messageInfo.getUserIds();

        List<Session> sessions = new ArrayList<Session>();
        for (String id : userIds) {
            sessions.add(WebSocketServer.socketMap.get(id));
        }

        if (sessions.contains(session)) {
            if (session != null && session.isOpen()) {
                session.sendText(messageInfo.getMessage());
            }
        }
    }


}
