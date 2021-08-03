package com.cs.demo.chain;

/**
 * @author: CS
 * @date: 2021/8/2 下午3:49
 * @description:
 */
public class NotSupportChainException extends RuntimeException {

    public NotSupportChainException() {
        super();
    }

    public NotSupportChainException(String message) {
        super(message);
    }
}
