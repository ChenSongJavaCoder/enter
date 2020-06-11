package com.cs.demo.thread;

/**
 * @Author: CS
 * @Date: 2020/3/26 6:05 下午
 * @Description:
 */
public class Thread1 extends Thread {

    public static void main(String[] args) {
        Thread1 thread1 = new Thread1();
        thread1.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("第" + i + "条数据");
        }
    }

}
