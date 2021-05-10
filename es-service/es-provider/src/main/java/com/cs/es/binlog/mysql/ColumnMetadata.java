package com.cs.es.binlog.mysql;

import lombok.Data;

/**
 * @author keosn
 * @date 2019/3/25 17:58
 */
@Data
public class ColumnMetadata{

    /**
     * 字段排序
     */
    Long order;

    /**
     * 字段名称
     */
    String name;

}
