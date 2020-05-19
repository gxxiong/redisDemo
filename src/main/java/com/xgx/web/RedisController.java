package com.xgx.web;

import com.xgx.websocket.MyWebSocket;
import com.xgx.websocket.PublishService;
import com.xgx.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PublishService publishService;

    @Autowired
    private MyWebSocket myWebSocket;

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
    public void test(String userId, String message) {
        boolean flag = myWebSocket.sendMessageToUser(userId, message);
        if (!flag) {
            publishService.publish("xgx", message);
        }
    }


}
