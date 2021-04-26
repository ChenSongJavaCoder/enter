package com.cs.common.test;

import com.cs.common.thread.ThreadExecutor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CS
 * @date: 2021/3/2 下午6:04
 * @description:
 */
public class ThreadLocalTest {

    static ThreadLocal<String> names = new ThreadLocal<>();

    static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            ThreadExecutor.execute(() -> {
                names.set("name" + atomicInteger.incrementAndGet());
                System.out.println("1" + names.get());
                System.out.println("2" + names.get());
            });
        }

    }

}
