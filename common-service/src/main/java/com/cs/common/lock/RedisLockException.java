package com.cs.common.lock;

/**
 * @author guanzhao
 */
public class RedisLockException extends RuntimeException {
    public RedisLockException(String message) {
        super(message);
    }
}
