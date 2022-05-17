package com.cs.common.annotation.validate;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.cs.common.util.GBKInvisibleCharacterUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/7/7 下午2:49
 * @description: 自定义校验非GBK字符提示语
 */
public class CustomGBKValueValidator implements ConstraintValidator<CustomGBKValue, String> {

    /**
     * 自定义模版消息，借鉴mysql中的sql语法检测
     */
    public static final String CUSTOM_MESSAGE_TEMPLATE = "提交的信息中包含不合法字符：[%s] 在[%s]附近";

    private String message;

    public static void main(String[] args) {
        String target = "山西省太原市迎泽区迎泽大街269号   0351-8950351℃,㎡,m³,°abc😊";
        List<String> unGBKCharacter = GBKInvisibleCharacterUtil.extractUnGBKCharacter(target);
        String join = StrUtil.join(",", unGBKCharacter);
        String customValidMessage = String.format(CUSTOM_MESSAGE_TEMPLATE, join, target.substring(target.indexOf(unGBKCharacter.get(0)), target.indexOf(unGBKCharacter.get(unGBKCharacter.size() - 1)) + 1));
        System.out.println("原文：" + target);
        System.out.println("提示：" + customValidMessage);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        List<String> unGBKCharacter = GBKInvisibleCharacterUtil.extractUnGBKCharacter(value);
        if (CollectionUtils.isEmpty(unGBKCharacter)) {
            return true;
        }
        // 禁用默认的提示消息
        context.disableDefaultConstraintViolation();
        String character = StrUtil.join(StrPool.COMMA, unGBKCharacter);
        String customValidMessage = String.format(CUSTOM_MESSAGE_TEMPLATE, character, value.substring(value.indexOf(unGBKCharacter.get(0)), value.indexOf(unGBKCharacter.get(unGBKCharacter.size() - 1))));
        // 使用自定义的提示消息
        context.buildConstraintViolationWithTemplate(customValidMessage).addConstraintViolation();
        return false;
    }

    @Override
    public void initialize(CustomGBKValue constraintAnnotation) {
        message = constraintAnnotation.message();
    }

}