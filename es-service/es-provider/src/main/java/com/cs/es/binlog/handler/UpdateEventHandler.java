package com.cs.es.binlog.handler;

import cn.hutool.json.JSONUtil;
import com.cs.es.binlog.bean.ColumnModifyBean;
import com.cs.es.binlog.bean.SourceTargetPair;
import com.cs.es.binlog.builder.DocumentMappingBuilder;
import com.cs.es.binlog.cache.RowValuesCache;
import com.cs.es.binlog.cache.RowValuesKeyProvider;
import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.DatabaseTablePair;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.converter.Converter;
import com.cs.es.binlog.converter.ConverterFactory;
import com.cs.es.binlog.mysql.ColumnMetadata;
import com.cs.es.binlog.mysql.TableMetadata;
import com.cs.es.binlog.mysql.TableMetadataBuilder;
import com.cs.es.binlog.mysql.TableMetadataCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description:
 */
@Slf4j
@Component
public class UpdateEventHandler implements Handler {

    private static final String TABLE_ID = "id";

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
        List<IndexQuery> indexQueries = new ArrayList<>();

        for (Map.Entry<Serializable[], Serializable[]> entry : updateRowsEventData.getRows()) {
            int idx = 1;
            // 组装数据
            Map<String, Serializable> beanMap = new LinkedHashMap<>();
            for (Serializable value : entry.getValue()) {
                beanMap.put(tableMetadata.getColumnMetadata().get(idx).getName(), value);
                idx++;
            }
            // 拿到数据录入缓存
            rowValuesCache.put(rowValuesKeyProvider.key(tableMetadata.getDatabase(), tableMetadata.getTable(), TABLE_ID, String.valueOf(beanMap.get(TABLE_ID))), beanMap);

            // 拿到对应的document class
            List<Class> javaClass = synchronizedConfiguration.getMappingDocumentClass(new DatabaseTablePair(tableMetadata.getDatabase(), tableMetadata.getTable()));
            javaClass.forEach(clazz -> {
                DocumentTableMapping documentTableMapping = new DocumentTableMapping(clazz, tableMetadata.getDatabase(), tableMetadata.getTable());
                String documentId = String.valueOf(beanMap.get(TABLE_ID));
                Object instance = documentMappingBuilder.build(documentTableMapping, beanMap);
                IndexQuery indexQuery = new IndexQueryBuilder()
                        // 对于符合数据库规范来说id即documentId
                        .withId(documentId)
                        .withObject(instance)
                        .build();
                indexQueries.add(indexQuery);
            });

            updateByRelateColumn(new DatabaseTablePair(tableMetadata.getDatabase(), tableMetadata.getTable()), entry, tableMetadata.getColumnMetadata());
        }

        // 更新对应数据
        elasticsearchRestTemplate.bulkIndex(indexQueries);

    }


    /**
     * 更新被关联字段的文档对象字段值
     *
     * @param databaseTablePair
     * @param entry
     */
    private void updateByRelateColumn(DatabaseTablePair databaseTablePair, Map.Entry<Serializable[], Serializable[]> entry, Map<Long, ColumnMetadata> columnMetadata) {
        // 关联该字段的类
        Map<Class, List<ColumnRelatedMapping>> relatedClass = synchronizedConfiguration.getRelatedClassMapping(databaseTablePair);
        if (CollectionUtils.isEmpty(relatedClass)) {
            return;
        }
        // 获取变动字段
        List<ColumnModifyBean> modifyColumns = new ArrayList<>();
        Serializable[] oldValues = entry.getKey();
        Serializable[] newValues = entry.getValue();
        // 暂不考虑数据库列变动的情况
        int index = Math.min(oldValues.length, newValues.length);
        for (int i = 0; i < index; i++) {
            if (!Objects.equals(oldValues[i], newValues[i])) {
                modifyColumns.add(new ColumnModifyBean(columnMetadata.get(Long.valueOf(i)).getName(), newValues[i], newValues[i]));
            }
        }
        log.info("数据变动：{}", JSONUtil.toJsonStr(modifyColumns));

        relatedClass.forEach((clazz, relateMapping) -> {
            StringBuffer scriptString = new StringBuffer();
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            for (ColumnRelatedMapping columnRelatedMapping : relateMapping) {
                // 关联字段值未做更新
                Optional<ColumnModifyBean> modifyColumn = modifyColumns.stream().filter(f -> f.getColumn().equals(columnRelatedMapping.getTargetColumn())).findFirst();
                if (!modifyColumn.isPresent()) {
                    continue;
                }
                Serializable newColumnValue = modifyColumn.get().getNewValue();
                Serializable oldColumnValue = modifyColumn.get().getOldValue();
                // 获取需要更新的值
                Object newFieldValue;
                Object oldFieldValue;
                Field field = null;
                Converter converter;
                try {
                    field = clazz.getDeclaredField(columnRelatedMapping.getFieldName());
                } catch (NoSuchFieldException e) {
                    log.error("获取类字段异常：", e);
                }
                log.info("column：{} modified, document：{}.{} need sync update!：", modifyColumn.get().getColumn(), clazz.getSimpleName(), field.getName());

                converter = converterFactory.getConverter(new SourceTargetPair(newColumnValue.getClass(), field.getType()));
                if (null != converter) {
                    newFieldValue = converter.convert(newColumnValue);
                    oldFieldValue = converter.convert(oldColumnValue);
                } else {
                    newFieldValue = newColumnValue;
                    oldFieldValue = oldColumnValue;
                }

                // 编写script脚本，注意脚本newFieldValue的属性问题
                String updateCodeTemplate = ScriptTemplate.buildScript(field.getName(), newFieldValue);
                scriptString.append(updateCodeTemplate);
                queryBuilder.filter(QueryBuilders.termQuery(field.getName(), oldFieldValue));
            }
            if (StringUtils.isEmpty(scriptString.toString())) {
                log.error("关联类未进行更新改动！");
                return;
            }
            Script script = new Script(scriptString.toString());
            ElasticsearchPersistentEntity entity = elasticsearchRestTemplate.getPersistentEntityFor(clazz);
            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
            updateByQueryRequest.indices(entity.getIndexName());
            updateByQueryRequest.setDocTypes(entity.getIndexType());
            updateByQueryRequest.setQuery(queryBuilder);
            updateByQueryRequest.setScript(script);
            updateByQueryRequest.setRefresh(true);
            updateByQueryRequest.setBatchSize(100);
            log.info("更新elasticsearchDSL：{}", JSONUtil.toJsonStr(updateByQueryRequest));
            try {
                BulkByScrollResponse response = elasticsearchRestTemplate.getClient().updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
                log.info("表【{}】 引起【{}】更新的结果：{}", databaseTablePair.getDatabase() + "." + databaseTablePair.getTable(), clazz, JSONUtil.toJsonStr(response));
            } catch (IOException e) {
                log.error("更新elasticsearch数据出错: ", e);
                e.printStackTrace();
            }
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
