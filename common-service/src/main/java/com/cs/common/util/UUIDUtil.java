package com.cs.common.util;

import java.util.UUID;

/**
 * @ClassName: UUIDUtil
 * @Author: CS
 * @Date: 2019/11/8 14:46
 * @Description:
 */
public class UUIDUtil {

	public static String uuid32() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
