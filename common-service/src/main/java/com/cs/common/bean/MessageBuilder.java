package com.cs.common.bean;

import java.util.Random;

/**
 * @ClassName: MessageBuilder
 * @Author: CS
 * @Date: 2019/9/12 17:27
 * @Description: just happiness
 */
public class MessageBuilder {

    private static String[] SUCCESS_MESSAGES = {"OK!", "GOOD!", "调用成功"};
    private static String[] FAILURE_MESSAGES = {"ERROR!", "出错啦!", "SOMETHING WRONG!"};

    private static Random RANDOM = new Random();

    public static String successMessage() {
        return SUCCESS_MESSAGES[RANDOM.nextInt(SUCCESS_MESSAGES.length)];
    }

    public static String failureMessage() {
        return FAILURE_MESSAGES[RANDOM.nextInt(FAILURE_MESSAGES.length)];
    }
}
