package com.cs.common.util;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * @Author: CS
 * @Date: 2020/10/26 14:48
 * @Description: 可作为校验对象使用（需配合validation框架使用）
 */
public class ValidateUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static String valid(Object obj, Class<?>... groups) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String message = null;
        if (groups == null) {
            groups = new Class[]{Default.class};
        }
        Set<ConstraintViolation<Object>> set = validator.validate(obj, groups);
        if (!CollectionUtils.isEmpty(set)) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> c : set) {
                // 校验有异常，直接返回     加入校验失败字段 2020/10/19
                sb.append(c.getPropertyPath()).append(c.getMessage()).append(" ");
            }
            return sb.toString();
        }
        for (Field field : fields) {
            if (field.getType() != String.class && !field.getType().isPrimitive() && field.getType() != List.class
                    && field.getType() != Integer.class && field.getType() != Double.class
                    && field.getType() != Boolean.class && field.getType() != Float.class
                    && field.getType() != Character.class && field.getType() != Long.class
                    && field.getType() != Byte.class && field.getType() != Short.class) {
                //非字符串类型非List的引用类型，递归
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    return valid(field.get(obj));
                }
            }
            if (field.getType() == List.class) {
                //list类型递归
                field.setAccessible(true);
                List list = (List) field.get(obj);
                if (list != null) {
                    for (Object sonObj : list) {
                        if (message != null) {
                            return message;
                        }
                        if (sonObj instanceof String) {
                            continue;
                        }
                        message = valid(sonObj);
                    }
                }
            }
        }
        return message;
    }

    public static class A{
        @NotNull
        String a;
        @NotNull
        @Min(0)
        Integer b;
    }


    public static void main(String[] args) throws IllegalAccessException {
        A a = new A();
        a.a = "a";

        String msg = valid(a);

    }
}
