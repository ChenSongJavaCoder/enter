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

    private String column;

    private String relatedDatabase;

    private String relatedTable;

    private String relatedColumn;

    private String relatedTargetColumn;

    private String targetColumn;
}