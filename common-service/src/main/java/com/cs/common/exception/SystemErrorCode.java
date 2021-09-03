package com.cs.common.exception;

/**
 * @author: CS
 * @date: 2021/1/26 下午5:07
 * @description: 系统异常码，不属于业务异常，比如说调用其他服务不可用时使用
 */
public enum SystemErrorCode implements BaseCode {

    DEMO(00000, "用户中心服务不可用"),
    NET_CONNECTION_ERROR(00001, "网络连接异常"),
    ;

    int code;
    String desc;

    SystemErrorCode(int code, String desc) {
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
