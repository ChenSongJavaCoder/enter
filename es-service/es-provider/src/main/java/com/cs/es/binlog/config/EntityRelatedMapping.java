package com.cs.es.binlog.config;

import com.cs.es.document.EsDocument;
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
     * 关联表的列名
     */
    private String relatedValueColumn;

    /**
     * 被关联表的列名
     */
    private String relatedTargetColumn;

    /**
     * 当前Document 注解的对象属性名
     */
    private String relatedField;


    private Class relatedClazz;


    private Class<? extends EsDocument> targetClazz;


}
