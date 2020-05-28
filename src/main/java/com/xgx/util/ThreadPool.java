package com.xgx.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadPool {

    /**
     * 自定义线程名称,方便的出错的时候溯源
     */
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("pitp-pool-%d").build();
    /**
     * 核心线程数
     */
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    public static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * corePoolSize    线程池核心池的大小
     * maximumPoolSize 线程池中允许的最大线程数量
     * keepAliveTime   当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
     * unit            keepAliveTime 的时间单位
     * workQueue       用来储存等待执行任务的队列
     * threadFactory   创建线程的工厂类
     * handler         拒绝策略类,当线程池数量达到上线并且workQueue队列长度达到上限时就需要对到来的任务做拒绝处理
     */
    private static ExecutorService service = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(200),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    public static ExecutorService getEs() {
        return service;
    }

    /**
     * 使用线程池创建线程并异步执行任务
     *
     * @param r 任务
     */
    public static void newTask(Runnable r) {
        service.execute(r);
    }

    public static ThreadPoolExecutor getThreadPoolStatus() {
        ThreadPoolExecutor tpe = ((ThreadPoolExecutor) service);
        return tpe;
    }

}
