package com.cs.common.annotation.validate;

import com.cs.common.util.GBKCharacterUtil;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author: CS
 * @date: 2021/7/7 下午2:52
 * @description:
 */
public class GBKValueValidator implements ConstraintValidator<GBKValue, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return !GBKCharacterUtil.containsUnGBKCharacter(value);
    }
}