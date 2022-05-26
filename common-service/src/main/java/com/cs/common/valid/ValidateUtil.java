package com.cs.common.valid;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.cs.common.annotation.validate.GBKValue;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.util.*;

/**
 * @Author: CS
 * @Date: 2020/10/26 14:48
 * @Description: 可作为校验对象使用，支持属性字段为对象的校验，即多层检验。需配合validation框架使用
 */
public class ValidateUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

//    private static List<Class> BASE_BOXED_TYPE = Arrays.asList(
//            Boolean.class, Character.class, String.class,
//            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigDecimal.class,
//            LocalDate.class, LocalDateTime.class, YearMonth.class
//    );


    public static String valid(Object obj, Class<?>... groups) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String message;
        if (groups == null) {
            groups = new Class[]{Default.class};
        }
        // 第一层校验
        Set<ConstraintViolation<Object>> set = validator.validate(obj, groups);
        if (!CollectionUtils.isEmpty(set)) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> c : set) {
                // 校验有异常，直接返回     加入校验失败字段 2020/10/19
                sb.append(c.getPropertyPath()).append(c.getMessage()).append("\n");
            }
            return sb.toString();
        }

        // 内部对象校验
        try {
            for (Field field : fields) {
                Class<?> type = field.getType();
                // 集合类需要优先处理
                if (Collection.class.isAssignableFrom(type)) {
                    field.setAccessible(true);
                    Collection collection = (Collection) field.get(obj);
                    if (collection != null) {
                        for (Object sonObj : collection) {
                            if (isBaseFieldType(sonObj.getClass())) {
                                continue;
                            }
                            message = valid(sonObj);
                            if (message != null) {
                                return message;
                            }
                        }
                    }
                }

                //非基础类型，递归
                if (!isBaseFieldType(field.getType())) {
                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        message = valid(field.get(obj));
                        if (message != null) {
                            return message;
                        }
                    }
                }


            }
        } catch (IllegalAccessException e) {
            ExceptionUtil.stacktraceToString(e);
        }
        return null;
    }


    private static boolean isBaseFieldType(Class<?> fieldType) {
        // 基础类型
        return fieldType.isPrimitive()
                // Boolean
                || fieldType == Boolean.class
                // 枚举
                || Enum.class.isAssignableFrom(fieldType)
                // 字符
                || fieldType == (Character.class)
                || fieldType == (String.class)
                // 数字
                || Number.class.isAssignableFrom(fieldType)
                // 日期
                || TemporalAdjuster.class.isAssignableFrom(fieldType)
                || fieldType == (Date.class)
                ;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setName("csm");
        user.setAge(10);
        user.setSex(Sex.MAN);
        WorkExperience workExperience = new WorkExperience();
        workExperience.setSerialNo(1);
        workExperience.setCompanyName("apple");
        workExperience.setStart(LocalDate.now());
        user.setWorkExperiences(Arrays.asList(workExperience));
        Profiles profiles = new Profiles();
        user.setProfiles(profiles);
        ProfilesB profilesB = new ProfilesB();
        profilesB.setLikeB("likeB");
        user.setProfilesB(profilesB);
        String msg = valid(user);
        System.out.println(msg);

    }

    enum Sex {
        MAN, WOMAN
    }

    @Data
    public static class User {
        @NotNull
        String name;
        @NotNull
        Integer age;
        @NotNull
        Sex sex;
        List<WorkExperience> workExperiences;
        @NotNull
        Profiles profiles;
        @NotNull
        ProfilesB profilesB;

    }

    @Data
    public static class WorkExperience {
        @NotNull
        @Min(1)
        Integer serialNo;
        @GBKValue
        @NotBlank
        String companyName;
        LocalDate start;
        LocalDate end;
    }

    @Data
    public static class Profiles {

        @NotBlank
        String like;

    }

    @Data
    public static class ProfilesB {

        @NotBlank
        String likeB;

    }
}
