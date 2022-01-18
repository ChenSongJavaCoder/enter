package com.cs.common.util;

import com.cs.common.exception.BizErrorCode;
import com.cs.common.exception.Throwing;

import java.util.Objects;

/**
 * @author: CS
 * @date: 2022/1/18 下午4:17
 * @description:
 */
public class Assert {

    /**
     * 基础判断
     *
     * @param isTrue
     * @param bizErrorCode
     */
    public static void isTrue(boolean isTrue, BizErrorCode bizErrorCode) {
        if (!isTrue) {
            Throwing.throwIt(bizErrorCode);
        }
    }

    /**
     * 校验策略上下文判断
     *
     * @param validatorContext
     * @param bizErrorCode
     * @param <T>
     */
    public static <T> void isTrue(ValidatorContext<T> validatorContext, BizErrorCode bizErrorCode) {
        isTrue(validatorContext.getValidator(), validatorContext.getValidData(), bizErrorCode);
    }

    /**
     * 校验器的判断
     *
     * @param validator
     * @param valid
     * @param bizErrorCode
     * @param <T>
     */
    public static <T> void isTrue(Validator<T> validator, T valid, BizErrorCode bizErrorCode) {
        isTrue(validator.validate(valid), bizErrorCode);
    }

    /**
     * 不为null判断
     *
     * @param o
     * @param bizErrorCode
     */
    public static void notNull(Object o, BizErrorCode bizErrorCode) {
        isTrue(Objects.nonNull(o), bizErrorCode);
    }
}
