package com.cs.common.exception;


/**
 * @author: CS
 * @date: 2021/9/3 下午2:22
 * @description:
 */
public class ThrowingTest {

    static BaseCodeException systemError = new BaseCodeException(SystemErrorCode.DEMO);


    public static void main(String[] args) {
        Throwing.throwIt(systemError);
    }

}
