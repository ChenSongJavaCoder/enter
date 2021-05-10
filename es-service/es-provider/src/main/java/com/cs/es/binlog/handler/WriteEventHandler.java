package com.cs.es.binlog.handler;

import com.cs.es.binlog.config.DatabaseTablePair;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.mysql.ColumnMetadata;
import com.cs.es.binlog.mysql.TableMetadata;
import com.cs.es.binlog.mysql.TableMetadataCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description: 插入数据事件处理
 */
@Slf4j
@Component
public class WriteEventHandler implements Handler {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TableMetadataCache tableMetadataCache;

    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;

    @Override
    public boolean support(Event event) {
        return EventType.isWrite(event.getHeader().getEventType());
    }

    @Override
    public void handle(Event event) {
        log.info("监听到写入事件：{}", event.toString());
        WriteRowsEventData writeRowsEventData = event.getData();

        TableMetadata tableMetadata = tableMetadataCache.get(writeRowsEventData.getTableId());

        if (null == tableMetadata) {
            return;
        }
        // 还原成数据库对象
        for (Serializable[] row : writeRowsEventData.getRows()) {
            Map<String, Serializable> beanMap = Maps.newLinkedHashMap();
            int j = 0;
            for (ColumnMetadata columnMetadata : tableMetadata.getColumnMetadata().values()) {
                beanMap.put(columnMetadata.getName(), row[j]);
                j++;
            }
            List<Class> javaClass = synchronizedConfiguration.getMappingDocumentClass(new DatabaseTablePair(tableMetadata.getDatabase(), tableMetadata.getTable()));
            if (!CollectionUtils.isEmpty(javaClass)) {
                try {
                    Object bean = objectMapper.readValue(objectMapper.writeValueAsString(beanMap), javaClass.get(0));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
