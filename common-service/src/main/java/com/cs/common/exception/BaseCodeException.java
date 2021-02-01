package com.cs.common.exception;

import lombok.Getter;

/**
 * @author: CS
 * @date: 2021/1/26 下午5:04
 * @description: 通用码异常，可以统一收集处理异常信息
 */
@Getter
public class BaseCodeException extends RuntimeException {

    private int code;
    private String desc;

    private BaseCodeException() {
    }

    /**
     * 只有一个BaseCode的构造函数
     *
     * @param baseCode
     */
    public BaseCodeException(BaseCode baseCode) {
        super(baseCode.getDesc());
        code = baseCode.getCode();
        desc = baseCode.getDesc();
    }
}
