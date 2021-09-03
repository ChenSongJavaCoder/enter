package com.cs.common.exception;

import lombok.SneakyThrows;

import java.util.function.Consumer;

/**
 * @author: CS
 * @date: 2021/9/3 下午2:17
 * @description: 重写consumer
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    @SneakyThrows
    @Override
    default void accept(T t) {
        try {
            accept0(t);
        } catch (Throwable throwable) {
            Throwing.sneakyThrow(throwable);
        }
    }

    ;

    void accept0(T t) throws Throwable;
}
