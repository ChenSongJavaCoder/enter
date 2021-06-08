package com.cs.es.binlog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author keosn
 * @date 2019/4/25 10:20
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(FIELD)
public @interface EntityRelated {

    /**
     * 被关联字段数据库名
     *
     * @return
     */
    String databaseName();

    /**
     * 被关联字段表明
     *
     * @return
     */
    String tableName();

    /**
     * related value of field, the field of current @Document
     *
     * @return
     */
    String relatedValueColumn();

    /**
     * The field of related entity
     *
     * @return
     */
    String relatedTargetColumn();


}
