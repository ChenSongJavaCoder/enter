package com.cs.common.exception;

import javax.validation.constraints.NotNull;

/**
 * @author: CS
 * @date: 2021/9/3 下午2:20
 * @description:
 */
public class Throwing {

    /**
     * BaseCodeException的异常抛出consumer
     */
    private static ThrowingConsumer<BaseCodeException> exceptionThrowingConsumer = e -> {
        throw e;
    };


    private Throwing() {
    }


    /**
     * 抛出异常通用错误码
     *
     * @param baseCode
     */
    public static void throwIt(BaseCode baseCode) {
        exceptionThrowingConsumer.accept(new BaseCodeException(baseCode));
    }


    /**
     * 抛出异常通用错误码
     *
     * @param
     */
    public static void throwIt(BaseCodeException ex) {
        exceptionThrowingConsumer.accept(ex);
    }

    /**
     * The compiler sees the signature with the throws T inferred to a RuntimeException type, so it allows the unchecked
     * exception to propagate.
     * <p>
     * http://www.baeldung.com/java-sneaky-throws
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Throwable> void sneakyThrow(@NotNull final Throwable ex) throws E {
        throw (E) ex;
    }
}
