package com.cs.es.binlog.builder;

import com.cs.common.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author: CS
 * @date: 2021/6/4 下午1:38
 * @description: elasticsearch更新脚本模版
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum ScriptTemplate {

    STRING_VALUE("ctx._source.%s='%s';"),
    NUMBER_VALUE("ctx._source.%s=%d;"),
    LOCAL_DATE_TIME_VALUE("ctx._source.%s='%s';"),
    NESTED_VALUE("ctx._source.%s=%s;"),
    ;

    private String template;

    public static String buildScript(String name, Object value) {
        String script;
        if (null == value) {
            script = String.format(NESTED_VALUE.getTemplate(), name, "null");
        } else if (value instanceof String && ((String) value).startsWith(DocumentMappingBuilder.NESTED_KEY)) {
            script = String.format(NESTED_VALUE.getTemplate(), name, value);
        } else if (value instanceof Number) {
            script = String.format(NUMBER_VALUE.getTemplate(), name, value);
        } else if (value instanceof Enum) {
            script = String.format(STRING_VALUE.getTemplate(), name, ((Enum) value).name());
        } else if (value instanceof LocalDateTime) {
            script = String.format(LOCAL_DATE_TIME_VALUE.getTemplate(), name, LocalDateTimeUtil.timeFormatPattern((LocalDateTime) value));
        } else if (value instanceof LocalDate) {
            script = String.format(LOCAL_DATE_TIME_VALUE.getTemplate(), name, LocalDateTimeUtil.dateFormatPattern((LocalDate) value));
        } else {
            script = String.format(STRING_VALUE.getTemplate(), name, value);
        }
        return script;
    }
}
