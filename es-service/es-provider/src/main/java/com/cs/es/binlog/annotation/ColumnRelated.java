package com.cs.es.binlog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author: CS
 * @date: 2021/5/8 下午6:11
 * @description: 关联表字段值
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(FIELD)
public @interface ColumnRelated {

    /**
     * 被关联字段数据库名
     * @return
     */
    String databaseName();

    /**
     * 被关联字段表明
     * @return
     */
    String tableName();

    /**
     * Document 中的关联字段
     * @return
     */
    String relatedColumn();

    /**
     * 被关联表中的关联字段
     * @return
     */
    String relatedTargetColumn();

    /**
     * 被关联表中的值字段， 该值将被更新到 Document 中的值字段
     * @return
     */
    String targetColumn();

}
