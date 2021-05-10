package com.cs.es.binlog.mysql;

import lombok.Data;

import java.util.Map;

/**
 * @author keosn
 * @date 2019/3/25 17:51
 */
@Data
public class TableMetadata {

    /**
     * binlog table id
      */
    Long tableId;

    /**
     * 库名
     */
    String database;

    /**
     * 表名
     */
    String table;

    /**
     * 列顺序 key value
     */
    Map<Long, ColumnMetadata> columnMetadata;

}
