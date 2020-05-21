package com.xgx.service.impl;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xgx.pojo.MessageInfo;
import com.xgx.pojo.User;
import com.xgx.pojo.WebsocketMsg;
import com.xgx.service.IThreadPoolService;
import com.xgx.util.ThreadPool;
import com.xgx.websocket.WebsocketPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class ThreadPoolServiceImpl implements IThreadPoolService {

    @Autowired
    private WebsocketPublish websocketPublish;


    @Override
    public void add() throws Exception {
        ThreadPool.getEs().execute(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebsocketMsg websocketMsg = new WebsocketMsg();
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setType("feiji");
            messageInfo.setMessage("我是飞机");
            websocketMsg.setMessageInfo(messageInfo);
            List<String> list = Lists.newArrayList("1","2","3");
            websocketMsg.setUserIds(list);

            websocketPublish.publish("xgx", websocketMsg);
            System.out.println(Thread.currentThread().getName());
        });

//        for (int i = 1; i <= 100; i++) {
//            int finalI = i;
//            threadPool.execute(() -> {
//                System.out.println(Thread.currentThread().getName());
//                iMatchingRuleDao.addMoney(finalI);
//            });
//        }

    }
}
