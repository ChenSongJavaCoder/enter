package com.tencent.wework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @Author: CS
 * @Date: 2020/3/24 5:07 下午
 * @Description:
 */
public class CommandTask implements Callable<ProcessResult> {

    private ExecutorService executorService;

    private String command;

    public CommandTask(ExecutorService executorService, String command) {
        this.executorService = executorService;
        this.command = command;
    }

    @Override
    public ProcessResult call() throws Exception {
        return CommandUtils.runCmdTest(executorService, command);
    }
}
