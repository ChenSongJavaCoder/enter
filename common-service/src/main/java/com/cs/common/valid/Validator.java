package com.cs.common.valid;

import com.cs.common.exception.BizErrorCode;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author: CS
 * @date: 2021/8/11 下午3:23
 * @description:
 */
public class Validator<T> {
    /**
     * 初始化为 true  true &&其它布尔值时由其它布尔值决定真假
     */
    private Predicate<T> predicate = t -> true;

    public static void main(String[] args) {
//        boolean validated = new Validator<String>()
//                .with(s -> s.length() > 5)
//                .with(s -> s.startsWith("cs"))
//                .with(s -> s.endsWith("cn"))
//                .with(s -> s.contains("."))
//                .validate("cs.cn");
//        System.out.println(validated);
//        if (!validated) {
//            Throwing.throwIt(BizErrorCode.DEMO);
//        }
//        Validator<String> validator = new Validator<String>()
//                .and(s -> s.length() > 5)
//                .or(s -> s.startsWith("cs") && s.contains("cn"));
//                .with(s -> s.endsWith("cn"))
//                .with(s -> s.contains("."));
//        Assert.isTrue(validator, "cs.cn", BizErrorCode.DEMO);

        Assert.isTrue(new ValidatorContext<String>("cs.cn", Objects::nonNull, t -> t.contains("cn")), BizErrorCode.NOT_NULL);
    }

    /**
     * 添加一个校验策略，可以无限续杯😀
     *
     * @param predicate the predicate
     * @return the validator
     */
    public Validator<T> and(Predicate<T> predicate) {
        this.predicate = this.predicate.and(predicate);
        return this;
    }

    /**
     * 添加一个校验策略，可以无限续杯😀
     *
     * @param predicate the predicate
     * @return the validator
     */
    public Validator<T> or(Predicate<T> predicate) {
        this.predicate = this.predicate.or(predicate);
        return this;
    }

    /**
     * 执行校验
     *
     * @param t the t
     * @return the boolean
     */
    public boolean validate(T t) {
        return predicate.test(t);
    }
}
