package com.cs.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @ClassName: ResultCode
 * @Author: CS
 * @Date: 2019/11/5 17:22
 * @Description: 返回状态码
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 也可使用自定义的code
     */
    SUCCESS(HttpStatus.OK.value(), "成功"),
    FAILURE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数据错误"),
    ILLEGAL(HttpStatus.BAD_REQUEST.value(), "参数不合法"),
    ;

    private int code;
    private String desc;
}
