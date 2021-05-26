package com.cs.es.binlog.builder;

import com.cs.es.binlog.cache.RowValuesCache;
import com.cs.es.binlog.cache.RowValuesKeyProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author keosn
 * @date 2019/4/3 17:29
 */
@Component
@Slf4j
public class RowValuesCacheGetter {

    @Autowired
    RowValuesCache rowValuesCache;

    @Autowired
    RowValuesKeyProvider rowValuesKeyProvider;


    public Serializable getValue(String database, String table, String relatedColumn, Object value, String valueColumn) {
        Map<String, Serializable> rowValue = getRowValue(database, table, relatedColumn, String.valueOf(value));

        return rowValue.get(valueColumn);
    }

    public Map<String, Serializable> getRowValue(String database, String table, String relatedColumn, Object value) {
        String key = rowValuesKeyProvider.key(database, table, relatedColumn, String.valueOf(value));
        Map<String, Serializable> rowValue = rowValuesCache.get(key);
        if (CollectionUtils.isEmpty(rowValue)) {
            log.warn("Got data consistency issue, key: {} - value column: {}", key);
            return Collections.emptyMap();
        }
        return rowValue;
    }

}
