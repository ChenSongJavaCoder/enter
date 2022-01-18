package com.cs.common.exception;

/**
 * @author: CS
 * @date: 2021/1/26 下午5:07
 * @description: 具体的业务异常，一般为已知的错误，检验不通过
 * 具体码规范可具体分配
 */
public enum BizErrorCode implements BaseCode {

    DEMO(10001, "业务校验错误"),
    NOT_NULL(10002, "业务数据为空"),
    ;

    int code;
    String desc;

    BizErrorCode(int code, String desc) {
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
