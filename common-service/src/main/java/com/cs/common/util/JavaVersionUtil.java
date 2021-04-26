package com.cs.common.util;

import org.springframework.boot.system.JavaVersion;

/**
 * @author: CS
 * @date: 2021/4/13 上午10:21
 * @description:
 */
public class JavaVersionUtil {

    public static void main(String[] args) {
        JavaVersion version = JavaVersion.getJavaVersion();
        version.name();
        System.out.println(String.format("name:%s toString: %s", version.name(), version.toString()));

    }

}
