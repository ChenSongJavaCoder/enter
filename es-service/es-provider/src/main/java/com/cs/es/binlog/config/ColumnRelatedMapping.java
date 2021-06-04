package com.cs.es.binlog.config;

/**
 * @author keosn
 * @date 2019/4/12 17:39
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ColumnRelatedMapping {
    /**
     * 类字段名称
     */
    private String fieldName;
    /**
     * 关联字段数据库名称
     */
    private String relatedDatabase;
    /**
     * 关联字段数据表名称
     */
    private String relatedTable;
    /**
     * 关联字段表字段
     */
    private String relatedColumn;
    /**
     * 被关联字段表字段 关联字段
     */
    private String relatedTargetColumn;
    /**
     * 被关联字段表字段，目标字段
     */
    private String targetColumn;
}