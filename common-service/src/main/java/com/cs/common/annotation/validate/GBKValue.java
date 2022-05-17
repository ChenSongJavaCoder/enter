package com.cs.common.annotation.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author: CS
 * @date: 2021/7/7 下午2:49
 * @description: 可通过扩展注解方式校验GBK非法字符
 * 扩展思考：可扩展至其他字符集范围的非法字符
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {GBKValueValidator.class})
public @interface GBKValue {

    String message() default "GBK包含非法字符，请确认！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
