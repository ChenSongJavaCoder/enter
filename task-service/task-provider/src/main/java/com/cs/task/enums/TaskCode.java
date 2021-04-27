package com.cs.task.enums;

import com.cs.common.exception.BaseCode;

/**
 * @author: CS
 * @date: 2021/4/26 下午9:23
 * @description:
 */
public enum TaskCode implements BaseCode {
    NOT_MATCH_BIZ_HANDLER(20001, "无匹配任务处理器"),
    ;

    private final int code;
    private final String desc;
    ;


    TaskCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
