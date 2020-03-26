package com.cs.wework.util;

import java.util.UUID;

/**
 * @Author: CS
 * @Date: 2020/3/19 5:39 下午
 * @Description:
 */
public class UUIDUtil {

    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
