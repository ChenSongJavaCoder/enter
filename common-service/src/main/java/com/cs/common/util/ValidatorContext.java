package com.cs.common.util;

import com.cs.common.exception.BizErrorCode;
import lombok.Getter;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author: CS
 * @date: 2022/1/18 下午5:00
 * @description:
 */
@Getter
public class ValidatorContext<T> {
    private Validator<T> validator;
    private T validData;

    public ValidatorContext(Validator<T> validator, T validData) {
        this.validator = validator;
        this.validData = validData;
    }

    public ValidatorContext(T validData, Predicate<T>... predicate) {
        Assert.notNull(predicate, BizErrorCode.NOT_NULL);
        this.validator = new Validator<>();
        Stream.of(predicate).forEach(e -> validator.with(e));
        this.validData = validData;
    }
}
