package com.cs.es.binlog.bean;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:36
 * @description:
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SourceTargetPair {
    private Class source;
    private Class target;
}
