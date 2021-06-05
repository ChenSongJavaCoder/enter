package com.cs.es.binlog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author keosn
 * @date 2019/4/12 17:40
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DatabaseTableColumn {
    private String database;
    private String table;
    private String cloumn;
}