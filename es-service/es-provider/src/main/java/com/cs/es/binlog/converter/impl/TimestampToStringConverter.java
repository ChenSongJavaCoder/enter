package com.cs.es.binlog.converter.impl;

import com.cs.es.binlog.converter.Converter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:55
 * @description: 数据库时间对象转字符串
 */
public class TimestampToStringConverter implements Converter<Timestamp, String> {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);

    @Override
    public String convert(Timestamp source) {
        // 默认0时间
        if (null == source) {
            return null;
        }
        return DATE_FORMAT.format(source);
    }
}
