package com.cs.es.binlog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author: CS
 * @date: 2021/6/4 上午10:42
 * @description:
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ColumnModifyBean {
    private String column;
    private Serializable oldValue;
    private Serializable newValue;
}
