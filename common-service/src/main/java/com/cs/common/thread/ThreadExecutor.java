package com.cs.common.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    }
}
