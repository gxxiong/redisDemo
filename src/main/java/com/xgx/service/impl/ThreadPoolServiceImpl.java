package com.xgx.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xgx.pojo.User;
import com.xgx.service.IThreadPoolService;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ThreadPoolServiceImpl implements IThreadPoolService {


    @Override
    public void add(User user) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
//                iMatchingRuleDao.addMoney(finalI);
            });
        }
    }
}
