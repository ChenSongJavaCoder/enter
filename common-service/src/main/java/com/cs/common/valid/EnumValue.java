package com.cs.common.valid;


import javax.validation.Constraint;
import java.lang.annotation.*;


/**
 * @author chensong
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
public @interface EnumValue {

    String message() default "必须为指定值";

    String[] strValues() default {};

    int[] intValues() default {};


    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumValue[] value();
    }
}