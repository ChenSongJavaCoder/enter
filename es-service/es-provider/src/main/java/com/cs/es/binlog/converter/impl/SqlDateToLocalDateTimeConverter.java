package com.cs.es.binlog.converter.impl;

import com.cs.es.binlog.converter.Converter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:55
 * @description: 数据库时间对象转字符串
 */
public class SqlDateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN);

    @Override
    public LocalDateTime convert(Date source) {
        // 默认0时间
        if (null == source) {
            return null;
        }
        //ES得按零时区转
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = DATE_FORMAT.format(source);
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(PATTERN));
    }
}
