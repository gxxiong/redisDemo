package com.xgx.web;

import com.google.common.collect.Lists;
import com.xgx.pojo.MessageInfo;
import com.xgx.pojo.User;
import com.xgx.pojo.WebsocketMsg;
import com.xgx.service.IThreadPoolService;
import com.xgx.websocket.WebSocketServer;
import com.xgx.websocket.WebsocketPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebsocketPublish websocketPublish;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private IThreadPoolService threadPoolService;

    @GetMapping("set")
    public void setOcr() {
        User user = new User();
        user.setId(1);
        user.setName("熊高祥");
        redisTemplate.opsForValue().set(1,user);
    }

    @GetMapping("get")
    public void getOcr() {
        User user = (User) redisTemplate.opsForValue().get(1);
        System.out.println(user);
    }

    @GetMapping("test")
    public String test(String userId, String message) throws Exception {
        threadPoolService.add();
        return "success";


//        boolean flag = myWebSocket.sendMessageToUser(userId, message);
//        if (!flag) {
//            publishService.publish("xgx", message);
//        }
    }


}
