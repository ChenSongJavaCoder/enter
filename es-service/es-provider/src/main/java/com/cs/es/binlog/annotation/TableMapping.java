package com.cs.es.binlog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: CS
 * @date: 2021/5/8 下午6:11
 * @description: 自定义数据库和表名
 * todo: 当前不支持分表下的es映射配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableMapping {
    /**
     * 数据库名称
     *
     * @return
     */
    String databaseName();

    /**
     * 表名,可不填，会自动依据类名转下划线生成
     * todo tableName通配符匹配'*'？指定规则eg：[0~10] 是否需要指定说明为分表
     *
     * @return
     */
    String tableName() default "";
}
