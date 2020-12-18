package com.xgx.service.impl;

import com.xgx.mapper.UserMapper;
import com.xgx.pojo.User;
import com.xgx.service.IThreadPoolService;
import com.xgx.util.ThreadPool;
import com.xgx.websocket.WebsocketPublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class ThreadPoolServiceImpl implements IThreadPoolService {

    @Autowired
    private WebsocketPublish websocketPublish;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void add() throws Exception {

        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行…… ……");
        ThreadPool.newTask(() -> {
            try {
                Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        ThreadPool.newTask(() -> {
            try {
                Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个子线程都执行完毕，继续执行主线程");

//        for (int i = 1; i <= 200; i++) {
//            ThreadPool.newTask(() -> {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName());
//                iMatchingRuleDao.addMoney(finalI);
//            });
//        }

    }

    @Override
    public void check() {
        ThreadPoolExecutor tpe = ((ThreadPoolExecutor) ThreadPool.getEs());

        int queueSize = tpe.getQueue().size();
        System.out.println("当前排队线程数：" + queueSize);

        int activeCount = tpe.getActiveCount();
        System.out.println("当前活动线程数：" + activeCount);

        long completedTaskCount = tpe.getCompletedTaskCount();
        System.out.println("执行完成线程数：" + completedTaskCount);

        long taskCount = tpe.getTaskCount();
        System.out.println("总线程数：" + taskCount);
    }

    @Override
    public User selectUserById(String userId) {
        User user= userMapper.selectUserById(userId);
        return user;
    }
}
