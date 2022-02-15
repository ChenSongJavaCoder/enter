package com.cs.common.thread;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: CS
 * @Date: 2020/4/22 2:25 下午
 * @Description:
 */
public class ThreadExecutor {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 这里指定为服务器线程数
            Runtime.getRuntime().availableProcessors(),
            30,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            (r)-> new Thread(r, "thread-pool-" + r.hashCode()),
            // 使用主线程调用，需要根据场景来设置，不然会引起主线程阻塞
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void execute(List<Runnable> runnable) {
        runnable.forEach(r -> executor.execute(r));
    }

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static void main(String[] args) {
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            AtomicInteger atomicInteger = new AtomicInteger();
//            while (atomicInteger.get() < 10) {
//                System.out.println(atomicInteger.incrementAndGet());
//            }
//        },5,10, TimeUnit.SECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            AtomicInteger atomicInteger = new AtomicInteger();
            while (atomicInteger.get() < 10) {
                System.out.println(atomicInteger.incrementAndGet());
            }
        }, 5, 10, TimeUnit.SECONDS);
    }
}
