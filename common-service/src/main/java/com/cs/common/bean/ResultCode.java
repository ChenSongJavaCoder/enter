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

	SUCCESS(1, "成功"),
	FAILURE(0, "失败"),
	;

	private int code;
	private String desc;
}
