package com.xgx.websocket;

import com.alibaba.fastjson.JSON;
import com.xgx.pojo.WebsocketMsg;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.yeauty.pojo.Session;

import java.util.List;


@Data
public class RedisMessageListener implements MessageListener {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageListener.class);

    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer;

    private Session session;


    /**
     * 订阅接收发布者的消息
     */
    public void onMessage(Message message, byte[] pattern) {
        WebsocketMsg websocketMsg = (WebsocketMsg) genericJackson2JsonRedisSerializer.deserialize(message.getBody());
        List<String> userIds = websocketMsg.getUserIds();

        for (String id : userIds) {
            List<Session> sessionList = WebSocketServer.socketMap.get(id);
            if (CollectionUtils.isNotEmpty(sessionList)) {
                for (Session session : sessionList) {
                    if (session != null && session.isOpen()) {
                        session.sendText(JSON.toJSONString(websocketMsg.getMessageInfo()));
                    }
                }
            }
        }
    }

}
