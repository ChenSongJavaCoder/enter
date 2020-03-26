package com.tencent.wework;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @Author: CS
 * @Date: 2020/3/24 5:04 下午
 * @Description:
 */
public class CommandUtils {

    public static ProcessResult runCmdTest(ExecutorService executorService, String command) throws InterruptedException {
        StringBuilder queryInputResult = new StringBuilder();
        StringBuilder queryErroInputResult = new StringBuilder();
        ProcessResult processResult = new ProcessResult();
        String[] cmd = {"/bin/sh", "-c", command};
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(cmd);
            CountDownLatch lock = new CountDownLatch(2);
            executorService.submit(new ProcessCheckTask(queryInputResult, lock, pro.getInputStream()));
            executorService.submit(new ProcessCheckTask(queryErroInputResult, lock, pro.getErrorStream()));
            boolean done = false;
            while (!done) {
                lock.await();
                done = true;
            }
            processResult.setOutputMessage(queryInputResult.toString());
            processResult.setErrorMessage(queryErroInputResult.toString());
            processResult.setSuccess(StringUtils.isEmpty(processResult.getErrorMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pro != null) {
                pro.destroy();
            }
        }
        return processResult;
    }

}
