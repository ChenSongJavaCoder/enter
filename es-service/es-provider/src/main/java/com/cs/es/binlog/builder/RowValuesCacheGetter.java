package com.cs.es.binlog.builder;

import com.cs.es.binlog.cache.RowValuesCache;
import com.cs.es.binlog.cache.RowValuesKeyProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
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
        String key = rowValuesKeyProvider.key(database, table, relatedColumn, String.valueOf(value));

        Map<String, Serializable> rowValue = rowValuesCache.get(key);
        if (null == rowValue || CollectionUtils.isEmpty(rowValue)) {
            log.warn("Got date consistency issue, key: {} - value column: {}", key, valueColumn);
            return null;
        }
        Serializable retVal = rowValuesCache.get(key).get(valueColumn);

        return retVal;
    }

    public Map<String, Serializable> getRowValue(String database, String table, String relatedColumn, Object value) {
        String key = rowValuesKeyProvider.key(database, table, relatedColumn, String.valueOf(value));
        return rowValuesCache.get(key);
    }

}
