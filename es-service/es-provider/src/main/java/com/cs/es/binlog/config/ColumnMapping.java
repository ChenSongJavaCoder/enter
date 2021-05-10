

package com.cs.es.binlog.config;

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
public class ColumnMapping {
    private String targetColumn;
    private String sourceColumn;
}
