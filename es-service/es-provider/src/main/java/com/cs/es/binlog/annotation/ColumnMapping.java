package com.cs.es.binlog.annotation;

import java.lang.annotation.*;

/**
 * @author: CS
 * @date: 2021/5/8 下午6:11
 * @description: 字段名，配合@see com.cs.es.binlog.annotation.TableMapping使用
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface ColumnMapping {
    /**
     * 字段名
     *
     * @return
     */
    String columnName();
}
