package com.cs.es.binlog.handler;

import cn.hutool.json.JSONUtil;
import com.cs.es.binlog.bean.ColumnModifyBean;
import com.cs.es.binlog.bean.DatabaseTablePair;
import com.cs.es.binlog.builder.DocumentMappingBuilder;
import com.cs.es.binlog.builder.UpdateByQueryActionListener;
import com.cs.es.binlog.builder.UpdateByQueryBuilder;
import com.cs.es.binlog.cache.RowValuesCache;
import com.cs.es.binlog.cache.RowValuesKeyProvider;
import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.converter.ConverterFactory;
import com.cs.es.binlog.mysql.TableMetadata;
import com.cs.es.binlog.mysql.TableMetadataBuilder;
import com.cs.es.binlog.mysql.TableMetadataCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description:
 */
@Slf4j
@Component
public class UpdateEventHandler implements Handler {

    private static final String TABLE_ID = "id";
    private static final String DOCUMENT_ID = TABLE_ID;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TableMetadataCache tableMetadataCache;

    @Autowired
    TableMetadataBuilder tableMetadataBuilder;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    DocumentMappingBuilder documentMappingBuilder;

    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;

    @Autowired
    RowValuesCache rowValuesCache;

    @Autowired
    RowValuesKeyProvider rowValuesKeyProvider;

    @Autowired
    ConverterFactory converterFactory;

    @Autowired
    UpdateByQueryBuilder updateByQueryBuilder;

    @Autowired
    UpdateByQueryActionListener updateByQueryActionListener;


    @Override
    public boolean support(Event event) {
        return EventType.isUpdate(event.getHeader().getEventType());
    }

    @Override
    public void handle(Event event) {
        Long start = System.currentTimeMillis();

        log.info("监听到更新事件：{}", event.toString());
        UpdateRowsEventData updateRowsEventData = event.getData();
        TableMetadata tableMetadata = tableMetadataCache.get(updateRowsEventData.getTableId());
        if (null == tableMetadata) {
            return;
        }
        for (Map.Entry<Serializable[], Serializable[]> entry : updateRowsEventData.getRows()) {
            Long idx = 1L;
            // 组装数据
            Map<String, Serializable> beanMap = new LinkedHashMap<>();
            for (Serializable value : entry.getValue()) {
                beanMap.put(tableMetadata.getColumnMetadata().get(idx).getName(), value);
                idx++;
            }
            // 拿到数据录入缓存
            rowValuesCache.put(rowValuesKeyProvider.key(tableMetadata.getDatabase(), tableMetadata.getTable(), TABLE_ID, String.valueOf(beanMap.get(TABLE_ID))), beanMap);

            // 获取变动字段
            List<ColumnModifyBean> modifyColumns = new ArrayList<>();
            Serializable[] oldValues = entry.getKey();
            Serializable[] newValues = entry.getValue();
            // 暂不考虑数据库列变动的情况
            int index = Math.min(oldValues.length, newValues.length);
            for (int i = 0; i < index; i++) {
                if (!Objects.equals(oldValues[i], newValues[i])) {
                    modifyColumns.add(new ColumnModifyBean(tableMetadata.getColumnMetadata().get(Long.valueOf(i + 1)).getName(), oldValues[i], newValues[i]));
                }
            }
            log.info("数据变动：{}", JSONUtil.toJsonStr(modifyColumns));
            DatabaseTablePair databaseTablePair = new DatabaseTablePair(tableMetadata.getDatabase(), tableMetadata.getTable());
            // 根据变动字段处理对应逻辑

            // 更新字段映射类
            updateByColumnMapping(databaseTablePair, modifyColumns, String.valueOf(beanMap.get(TABLE_ID)));

            // 更新关联字段映射类
            updateByRelateColumn(databaseTablePair, modifyColumns, beanMap);
        }

        log.error("更新事件,数据条数: {} 同步耗时: {}", updateRowsEventData.getRows().size(), (System.currentTimeMillis() - start));


    }


    /**
     * 更新关联字段的文档对象字段值
     *
     * @param databaseTablePair
     */
    private void updateByColumnMapping(DatabaseTablePair databaseTablePair, List<ColumnModifyBean> modifyColumns, String documentId) {
        List<Class> javaClass = synchronizedConfiguration.getMappingDocumentClass(databaseTablePair);
        if (CollectionUtils.isEmpty(javaClass)) {
            return;
        }

        javaClass.forEach(clazz -> {
            Map<String, String> fieldColumnMap = synchronizedConfiguration.getColumnMapping(new DocumentTableMapping(clazz, databaseTablePair.getDatabase(), databaseTablePair.getTable()));
            // 根据主键更新
            UpdateByQueryRequest updateByQueryRequest = updateByQueryBuilder.buildUpdateByRequest(clazz, modifyColumns, fieldColumnMap, QueryBuilders.termQuery(DOCUMENT_ID, documentId));
            elasticsearchRestTemplate.getClient().updateByQueryAsync(updateByQueryRequest, RequestOptions.DEFAULT, updateByQueryActionListener);
        });
    }


    /**
     * 更新被关联的类
     *
     * @param databaseTablePair
     * @param modifyColumns
     * @param beanMap
     */
    private void updateByRelateColumn(DatabaseTablePair databaseTablePair, List<ColumnModifyBean> modifyColumns, Map<String, Serializable> beanMap) {
        // 关联该字段的类
        Map<Class, List<ColumnRelatedMapping>> relatedClass = synchronizedConfiguration.getRelatedClassMapping(databaseTablePair);
        if (CollectionUtils.isEmpty(relatedClass)) {
            return;
        }

        relatedClass.forEach((clazz, relatedMappings) -> {
            //根据关联字段值更新
            ColumnRelatedMapping relatedMapping = relatedMappings.get(0);
            String documentFieldName = relatedMapping.getFieldName();
            String relatedValueField = relatedMapping.getRelatedColumn().replace("@", "");
            Serializable relatedValue = beanMap.get(relatedMapping.getRelatedTargetColumn());
            if (null == relatedValue) {
                log.error("document: {},字段: {},关联值:null！将导致关联更新失败！", clazz, documentFieldName);
                return;
            }
            QueryBuilder queryBuilder = QueryBuilders.termQuery(relatedValueField, relatedValue);
            Map<String, String> fieldColumnMap = relatedMappings.stream().collect(Collectors.toMap(ColumnRelatedMapping::getFieldName, ColumnRelatedMapping::getTargetColumn));
            UpdateByQueryRequest updateByQueryRequest = updateByQueryBuilder.buildUpdateByRequest(clazz, modifyColumns, fieldColumnMap, queryBuilder);
            elasticsearchRestTemplate.getClient().updateByQueryAsync(updateByQueryRequest, RequestOptions.DEFAULT, updateByQueryActionListener);
        });
    }

    /**
     * 更新被关联字段的文档对象字段对象
     *
     * @param databaseTablePair
     * @param entry
     */
    private void updateByRelateEntity(DatabaseTablePair databaseTablePair, Map.Entry<Serializable[], Serializable[]> entry) {

    }

}
