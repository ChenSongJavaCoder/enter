package com.cs.es.binlog.converter.impl;

import com.cs.es.binlog.converter.Converter;

/**
 * @author: CS
 * @date: 2021/5/25 下午6:03
 * @description:
 */
public class IntegerToLongConverter implements Converter<Integer, Long> {

    @Override
    public Long convert(Integer source) {
        if (null == source) {
            return null;
        }
        return source.longValue();
    }
}
