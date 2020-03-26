package com.tencent.wework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CS
 * @Date: 2020/3/24 5:06 下午
 * @Description:
 */
public class CommandHelper {

    private static Logger logger = LoggerFactory.getLogger(CommandHelper.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    private static long default_timeout = 8000;

    public static ProcessResult process(String command) {
        return process(command, default_timeout, TimeUnit.MILLISECONDS);
    }

    public static ProcessResult process(String command, long timeout, TimeUnit unit) {
        CommandTask commandTask = new CommandTask(executorService, command);
        Future<ProcessResult> processResult = executorService.submit(commandTask);
        ProcessResult result = null;
        try {
            result = processResult.get(timeout, unit);
        } catch (Exception e) {
            logger.error("command error");
        }
        return result;
    }

}
