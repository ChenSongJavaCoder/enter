package com.cs.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author chensong
 * @description: 支持字符枚举和int类型枚举值验证 如果只是字符串，可以使用枚举类
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    /**
     * 支持字符串数组
     */
    private String[] strValues;
    /**
     * 支持的数字数组
     */
    private int[] intValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            for (String s : strValues) {
                if (Objects.equals(s, value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer) {
            for (Integer s : intValues) {
                if (Objects.equals(s, value)) {
                    return true;
                }
            }
        }
        return false;

    }


}