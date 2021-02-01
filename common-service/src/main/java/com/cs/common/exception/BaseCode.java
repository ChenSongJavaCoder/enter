package com.cs.common.exception;

/**
 * @author: CS
 * @date: 2021/1/26 下午4:54
 * @description: 基础码，统一规划业务上的异常码
 */
public interface BaseCode {

    /**
     * 异常码
     *
     * @return
     */
    int getCode();

    /**
     * 描述信息
     *
     * @return
     */
    String getDesc();
}
