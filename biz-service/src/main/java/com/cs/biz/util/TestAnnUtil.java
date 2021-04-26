package com.cs.biz.util;

import com.cs.biz.aop.TestAnn;

/**
 * @author: CS
 * @date: 2021/4/12 上午10:21
 * @description:
 */
public class TestAnnUtil {


    @TestAnn
    public static String testAnn() {
        return "abc";
    }
}
