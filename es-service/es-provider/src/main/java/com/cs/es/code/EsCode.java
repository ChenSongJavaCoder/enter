package com.cs.es.code;

import com.cs.common.exception.BaseCode;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:35
 * @description:
 */
public enum EsCode implements BaseCode {

    NOT_MATCH_HANDLER(50001, "该binlog事件暂未支持"),
    ;

    int code;
    String desc;

    EsCode(int code, String desc) {
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
