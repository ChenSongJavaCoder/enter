package com.cs.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:08
 * @description: 任务类型
 * @see com.cs.task.handler.BizHandler 每种任务一一对应
 */
@Getter
@AllArgsConstructor
public enum TaskType {

    A(1, "A"),
    B(2, "B"),
    ;

    /**
     * 类型代码
     */
    private final Integer code;
    /**
     * 任务描述
     */
    private final String desc;
}
