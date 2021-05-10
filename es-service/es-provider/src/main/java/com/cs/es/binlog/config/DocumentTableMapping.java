package com.cs.es.binlog.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author keosn
 * @date 2019/4/12 17:36
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DocumentTableMapping {

    private Class documentClass;

    private String database;

    private String table;
}
