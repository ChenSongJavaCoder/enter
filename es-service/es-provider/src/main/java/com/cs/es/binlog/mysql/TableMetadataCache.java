package com.cs.es.binlog.mysql;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keosn
 * @date 2019/3/25 17:53
 */
@Component
public class TableMetadataCache {

    private static final Map<Long, TableMetadata> cache = new ConcurrentHashMap<>();

    public TableMetadata get(Long identity) {
        return cache.get(identity);
    }

    public TableMetadata put(TableMetadata tableMetadata) {
        return cache.put(tableMetadata.getTableId(), tableMetadata);
    }
}
