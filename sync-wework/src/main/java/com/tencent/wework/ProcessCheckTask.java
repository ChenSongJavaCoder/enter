package com.tencent.wework;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: CS
 * @Date: 2020/3/24 5:05 下午
 * @Description:
 */
public class ProcessCheckTask implements Runnable {


    /**
     * 锁
     */
    private CountDownLatch lock;

    /**
     * 执行结果输入流
     */
    private InputStream inputStream;

    /**
     * 字符拼接
     */
    private StringBuilder queryInputResult;

    public ProcessCheckTask(StringBuilder queryInputResult, CountDownLatch lock, InputStream inputStream) {
        super();
        this.lock = lock;
        this.inputStream = inputStream;
        this.queryInputResult = queryInputResult;
    }

    @Override
    public void run() {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bf.readLine()) != null && line.length() > 0) {
                queryInputResult.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.countDown();
        }
    }
}