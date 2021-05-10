package com.cs.es.binlog.handler;

import com.cs.es.binlog.config.DatabaseTablePair;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.mysql.TableMetadata;
import com.cs.es.binlog.mysql.TableMetadataBuilder;
import com.cs.es.binlog.mysql.TableMetadataCache;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description:
 */
@Slf4j
@Component
public class MapEventHandler implements Handler {

    @Autowired
    TableMetadataBuilder tableMetadataBuilder;

    @Autowired
    TableMetadataCache tableMetadataCache;

    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;

    @Override
    public boolean support(Event event) {
        return Objects.equals(EventType.TABLE_MAP, event.getHeader().getEventType());
    }

    @Override
    public void handle(Event event) {
        TableMapEventData tableMapEventData = event.getData();
        DatabaseTablePair databaseTablePair = new DatabaseTablePair(tableMapEventData.getDatabase(), tableMapEventData.getTable());
        // 对于不需要映射的表进行过滤
        if (!synchronizedConfiguration.containsDatabaseTablePair(databaseTablePair)) {
            return;
        }

        TableMetadata tableMetadata = tableMetadataCache.get(tableMapEventData.getTableId());
        if (null == tableMetadata) {
            tableMetadata = tableMetadataBuilder.build(tableMapEventData.getDatabase(), tableMapEventData.getTable(), tableMapEventData.getTableId());
            tableMetadataCache.put(tableMetadata);
            log.info("Build {}.{}.{} table map success !", tableMetadata.getDatabase(), tableMetadata.getTable(), tableMetadata.getTableId());
        }
    }
}
