package com.cs.task.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:08
 * @description:
 */
@Getter
@AllArgsConstructor
public enum TaskState {
    TASK_INIT(1, "待执行"),
    TASK_PROCESSING(2, "任务执行中"),
    TASK_PROCESSING_EXP(3, "任务执行中异常中断"),
    TASK_CALLBACK(4, "任务等待回调中"),
    TASK_DONE(5, "任务执行完毕"),
    BUSINESS_PROCESSING(6, "业务处理中"),
    BUSINESS_SUC(7, "业务处理成功"),
    BUSINESS_FAIL(8, "业务处理失败"),
    ;

    /**
     * 类型代码
     */
    private final Integer code;
    /**
     * 任务描述
     */
    private final String desc;

    /**
     * 初始状态
     *
     * @return
     */
    public static TaskState initState() {
        return TASK_INIT;
    }

    /**
     * 最终状态
     *
     * @return
     */
    public static List<TaskState> terminalState() {
        return Lists.newArrayList(TASK_PROCESSING_EXP, BUSINESS_SUC, BUSINESS_FAIL);
    }

}
