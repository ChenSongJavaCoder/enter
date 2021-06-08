package com.cs.es.binlog.handler;

import com.cs.es.binlog.bean.DatabaseTablePair;
import com.cs.es.binlog.builder.DocumentMappingBuilder;
import com.cs.es.binlog.cache.RowValuesCache;
import com.cs.es.binlog.cache.RowValuesKeyProvider;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.mysql.ColumnMetadata;
import com.cs.es.binlog.mysql.TableMetadata;
import com.cs.es.binlog.mysql.TableMetadataCache;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description: 插入数据事件处理
 */
@Slf4j
@Component
public class WriteEventHandler implements Handler {

    private static final String TABLE_ID = "id";


    @Autowired
    TableMetadataCache tableMetadataCache;

    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    DocumentMappingBuilder documentMappingBuilder;

    @Autowired
    RowValuesCache rowValuesCache;

    @Autowired
    RowValuesKeyProvider rowValuesKeyProvider;

    @Override
    public boolean support(Event event) {
        return EventType.isWrite(event.getHeader().getEventType());
    }

    @Override
    public void handle(Event event) {
        long start = System.currentTimeMillis();
        log.debug("监听到写入事件：{}", event.toString());
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

            // 拿到数据录入缓存 eg: key=local:mine:user_role:user_id:3
            rowValuesCache.put(rowValuesKeyProvider.key(tableMetadata.getDatabase(), tableMetadata.getTable(), TABLE_ID, String.valueOf(beanMap.get(TABLE_ID))), beanMap);

            // 拿到对应的document class
            List<Class> javaClass = synchronizedConfiguration.getMappingDocumentClass(new DatabaseTablePair(tableMetadata.getDatabase(), tableMetadata.getTable()));
            List<IndexQuery> indexQueries = new ArrayList<>();
            javaClass.forEach(clazz -> {
                // 单表可以直接进行映射转换，对于关联组合字段需要独立逻辑处理 objectMapper.readValue()
                // 关联表数据更新，存在先后关系
                DocumentTableMapping documentTableMapping = new DocumentTableMapping(clazz, tableMetadata.getDatabase(), tableMetadata.getTable());
                Object instance = documentMappingBuilder.build(documentTableMapping, beanMap);
                IndexQuery indexQuery = new IndexQueryBuilder()
                        // 对于符合数据库规范来说id即documentId
                        .withId(Objects.nonNull(beanMap.get(TABLE_ID)) ? String.valueOf(beanMap.get(TABLE_ID)) : null)
                        .withObject(instance)
                        .build();
                indexQueries.add(indexQuery);

            });
            elasticsearchRestTemplate.bulkIndex(indexQueries);
            log.error("写入事件,数据条数: {} 同步耗时: {}", writeRowsEventData.getRows().size(), (System.currentTimeMillis() - start));
        }
    }
}
