package com.cs.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: ResultCode
 * @Author: CS
 * @Date: 2019/11/5 17:22
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "成功"),
    FAILURE(500, "失败"),
    ILLEGAL(400, "参数不合法"),
	;

	private int code;
	private String desc;
}
