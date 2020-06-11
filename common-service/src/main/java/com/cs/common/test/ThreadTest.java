package com.cs.common.test;

import com.cs.common.thread.ThreadExecutor;

/**
 * @Author: CS
 * @Date: 2020/4/22 2:43 下午
 * @Description:
 */
public class ThreadTest {


    public static void main(String[] args) {
//        ThreadExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("线程1--start");
//            }
//        });
//        List<Runnable> list = new ArrayList<>();
//        for (int i = 0; i < 60; i++) {
//            final int idx = i;
//            list.add(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("线程 " + idx + "开始");
//                }
//            });
//        }
//        ThreadExecutor.execute(list);

        class Thread1 extends Thread {
            @Override
            public void run() {
                System.out.println("thread1 -------");
            }
        }
        ThreadExecutor.execute(new Thread1());


    }
}
