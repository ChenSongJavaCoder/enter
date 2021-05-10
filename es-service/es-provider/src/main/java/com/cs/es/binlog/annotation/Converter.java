package com.cs.es.binlog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:31
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(FIELD)
public @interface Converter {
    /**
     * 类型转换器
     * @return
     */
  Class <? extends com.cs.es.binlog.converter.Converter>  value();
}
