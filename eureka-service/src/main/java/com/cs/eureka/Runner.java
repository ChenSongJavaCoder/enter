package com.cs.eureka;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: CS
 * @Date: 2020/7/1 2:10 下午
 * @Description:
 */
@Component
public class Runner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        int idx = 1;
//        while (true) {
//
//            for (int i = 0; i < 10; i++) {
//                idx++;
//                int finalIdx = idx;
//                Runnable run = new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println(Thread.currentThread().getId() + Thread.currentThread().getName() + " || "+finalIdx);
//                    }
//                };
//                ThreadExecutor.execute(run);
//            }
//
//            Thread.sleep(1000);
//        }

    }
}
