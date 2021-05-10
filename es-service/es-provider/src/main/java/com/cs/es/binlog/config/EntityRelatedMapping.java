package com.cs.es.binlog.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author keosn
 * @date 2019/4/25 11:11
 */
@Data
@AllArgsConstructor
public class EntityRelatedMapping {

    /**
     * 被关联数据库schema
     */
    private String database;

    /**
     * 被关联数据库表名
     */
    private String tableName;

    /**
     * 被关联表的列名
     */
    private String targetField;

    /**
     * 当前Document 中提供的关联值属性（注：用这个值过滤产生关联数据）
     */
    private String valueField;

    /**
     * 当前Document 注解的对象属性名
     */
    private String relatedField;


}
