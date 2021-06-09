package com.cs.es.binlog.converter.impl;

import com.cs.es.binlog.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:55
 * @description: 数据库时间对象转字符串
 */
public class DateToStringConverter implements Converter<Date, String> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String NULL_DATE_VALUE = "0000-00-00 00:00:00";

    @Override
    public String convert(Date source) {
        // 默认0时间
        if (null == source) {
            return NULL_DATE_VALUE;
        }
        //ES得按零时区转
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        return DATE_FORMAT.format(source);
    }
}
