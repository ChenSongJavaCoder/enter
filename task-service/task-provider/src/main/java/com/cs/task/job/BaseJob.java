package com.cs.task.job;

import com.cs.common.exception.BaseCodeException;
import com.cs.task.entity.Task;
import com.cs.task.enums.TaskCode;
import com.cs.task.enums.TaskState;
import com.cs.task.enums.TaskType;
import com.cs.task.handler.BizHandler;
import com.cs.task.handler.BizHandlerDispatcher;
import com.cs.task.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * @author: CS
 * @date: 2021/4/26 下午9:33
 * @description:
 */
@Slf4j
public abstract class BaseJob {

    @Autowired
    BizHandlerDispatcher dispatcher;

    @Autowired
    TaskHandler taskHandler;

    /**
     * 记录当前线程所持有的handler
     */
    ThreadLocal<BizHandler> handlers = new ThreadLocal<>();

    /**
     * 获取任务处理器
     *
     * @return
     */
    private BizHandler getHandler() {
        Optional<BizHandler> handler = dispatcher.getSupportedHandler(getType());
        if (handler.isPresent()) {
            handlers.set(handler.get());
            return handlers.get();
        }
        throw new BaseCodeException(TaskCode.NOT_MATCH_BIZ_HANDLER);
    }

    /**
     * 执行初始化任务
     */
    public void executeInitTasks() {
        List<Task> initTasks = taskHandler.findAllTasks(getType(), TaskState.TASK_INIT);
        initTasks.stream().forEach(e -> taskHandler.executeSingleTask(e, getHandler()));
    }

    /**
     * 执行业务任务
     */
    public void executeBizTasks() {
        List<Task> initTasks = taskHandler.findAllTasks(getType(), TaskState.TASK_DONE);
        initTasks.stream().forEach(e -> taskHandler.executeSingleTaskBiz(e, getHandler()));
    }

    /**
     * 获取任务执行类型，用于子类实现
     *
     * @return
     */
    public abstract TaskType getType();

}
