package com.cs.task.job;

import com.cs.task.enums.TaskType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/4/27 下午3:28
 * @description:
 */
@Slf4j
@Component
public class AJob extends BaseJob {

    @Override
    public TaskType getType() {
        return TaskType.A;
    }

    @XxlJob("aInitTask")
    public ReturnT<String> aInitTask(String param) {
        super.executeInitTasks();
        return ReturnT.SUCCESS;
    }

    @XxlJob("aBizTask")
    public ReturnT<String> aBizTask(String param) {
        super.executeBizTasks();
        return ReturnT.SUCCESS;
    }

}
