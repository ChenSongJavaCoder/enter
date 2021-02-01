package com.cs.user.pojo;

import com.cs.common.exception.BaseCode;

/**
 * @author: CS
 * @date: 2021/1/26 下午5:07
 * @description: 用户中心异常码
 */
public enum UserCenterCode implements BaseCode {

    USER_NOT_FOUND(10001, "用户未注册"),
    USER_EXISTED(10002, "用户已注册"),
    ;

    int code;
    String desc;

    UserCenterCode(int code, String desc) {
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
