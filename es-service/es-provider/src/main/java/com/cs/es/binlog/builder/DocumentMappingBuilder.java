package com.cs.es.binlog.builder;

import cn.hutool.json.JSONUtil;
import com.cs.es.binlog.bean.DatabaseTableColumn;
import com.cs.es.binlog.bean.Null;
import com.cs.es.binlog.bean.SourceTargetPair;
import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.EntityRelatedMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.converter.Converter;
import com.cs.es.binlog.converter.ConverterFactory;
import com.cs.es.binlog.handler.ScriptTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: CS
 * @date: 2021/5/22 下午9:42
 * @description: 构建document实例
 */
@Slf4j
@Component
public class DocumentMappingBuilder {

    public static final String LOCAL_FIELD_PREFIX = "@";
    public static final String NESTED_KEY = "params";
    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;
    @Autowired
    RelatedValueGetter relatedValueGetter;
    @Autowired
    ConverterFactory converterFactory;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 构建实例对象
     *
     * @param documentTableMapping 文档数据库表映射
     * @param keyValues            数据
     * @param <T>                  实例对象
     * @return
     */
    public <T> T build(DocumentTableMapping documentTableMapping, Map<String, Serializable> keyValues) {
        Class<T> clazz = documentTableMapping.getDocumentClass();
        // 字段映射
        Map<String, String> columnMappings = synchronizedConfiguration.getColumnMapping(documentTableMapping);
        // 关联字段映射
        Map<String, ColumnRelatedMapping> relatedColumnMappings = synchronizedConfiguration.getRelatedColumnMapping(clazz);
        // 关联实体映射
        Map<String, EntityRelatedMapping> entityRelatedColumnMappings = synchronizedConfiguration.getEntityRelatedColumnMapping(clazz);
        // 字段值缓存
        Map<String, Serializable> currentFieldCache = new HashMap<>(keyValues.size());
        // 返回的实体对象
        T t = null;
        try {
            t = clazz.newInstance();
            if (null != t) {
                List<String> beingRelatedColumn = new ArrayList<>();
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    // 映射字段
                    String column = null;
                    if (columnMappings.keySet().contains(field.getName())) {
                        log.trace("Got  mapping column {}", field.getName());
                        column = columnMappings.get(field.getName());
                        Serializable value = keyValues.get(column);
                        Class<?> valueClass = null == value ? Null.class : value.getClass();
                        Converter converter = getConverter(field, valueClass);
                        if (null != converter) {
                            field.set(t, converter.convert(value));
                            currentFieldCache.put(field.getName(), value);
                        } else {
                            if (null != value) {
                                field.set(t, value);
                                currentFieldCache.put(field.getName(), value);
                            }
                        }


                    }
                    // 关联字段映射
                    if (null != relatedColumnMappings && relatedColumnMappings.keySet().contains(field.getName())) {
                        log.info("Got related mapping column {}", field.getName());
                        ColumnRelatedMapping relatedColumnMapping = relatedColumnMappings.get(field.getName());
                        // 获取关联字段值 类似'join on'中的'on'字段值
                        Serializable relatedValue;
                        String relatedCol = relatedColumnMapping.getRelatedColumn();
                        if (relatedCol.startsWith(LOCAL_FIELD_PREFIX)) {
                            relatedValue = currentFieldCache.get(relatedCol.replaceFirst(LOCAL_FIELD_PREFIX, StringUtils.EMPTY));
                        } else {
                            relatedValue = keyValues.get(relatedCol);
                        }
                        // 获取关联值
                        Serializable value = relatedValueGetter.getValue(relatedColumnMapping, relatedValue);
                        Class<?> valueClass = null == value ? Null.class : value.getClass();
                        Converter converter = getConverter(field, valueClass);
                        if (null != converter && null != value) {
                            field.set(t, converter.convert(value));
                            currentFieldCache.put(field.getName(), value);
                        } else {
                            if (null != value) {
                                field.set(t, value);
                                currentFieldCache.put(field.getName(), value);
                            }
                        }
                    }
                    // 关联对象
//                    if (null != entityRelatedColumnMappings && entityRelatedColumnMappings.keySet().contains(field.getName())) {
//                        EntityRelatedMapping entityRelatedMapping = entityRelatedColumnMappings.get(field.getName());
//                        Object relatedValue = currentFieldCache.get(entityRelatedMapping.getRelatedTargetColumn());
//                        Map<String, Serializable> value = relatedValueGetter.getRowValue(entityRelatedMapping, relatedValue);
//                        field.set(t, value);
//                    }
                    if (Objects.nonNull(column)) {
                        beingRelatedColumn.add(column);
                    }
                }
                for (String column : beingRelatedColumn) {
                    updateRelatedDocument(t, new DatabaseTableColumn(documentTableMapping.getDatabase(), documentTableMapping.getTable(), column), keyValues);
                }
            }
        } catch (InstantiationException e) {
            log.error("InstantiationException:", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException:", e);
        } catch (Exception e) {
            log.error("Exception:", e);
        }

        return t;
    }

    private <T> void updateRelatedDocument(T t, DatabaseTableColumn databaseTableColumn, Map<String, Serializable> keyValues) {
        List<UpdateByQueryRequest> updateByQueryRequests = new ArrayList<>();
        // 关联该字段类更新
        String columnKey = synchronizedConfiguration.columnKey(databaseTableColumn.getDatabase(), databaseTableColumn.getTable(), databaseTableColumn.getCloumn());
        Map<Class, ColumnRelatedMapping> columnRelatedClass = synchronizedConfiguration.getColumnRelatedClassMapping(columnKey);
        if (null != columnRelatedClass) {
            columnRelatedClass.forEach((tClass, columnRelatedMapping) -> {
                Serializable value = keyValues.get(databaseTableColumn.getCloumn());
                // 被关联字段值
                String relateColumn = columnRelatedMapping.getRelatedTargetColumn();
                Serializable relateValue = keyValues.get(relateColumn);
                // 目标类字段，一般约定为主键id
                String targetRelateColumn = columnRelatedMapping.getRelatedColumn();
                String targetFieldName = columnRelatedMapping.getFieldName();

                String relateClassUpdateScript = ScriptTemplate.buildScript(targetFieldName, value);
                Script script = new Script(relateClassUpdateScript);
                TermQueryBuilder queryBuilder = QueryBuilders.termQuery(targetRelateColumn, relateValue);
                UpdateByQueryRequest updateByQueryRequest = buildUpdateByRequest(tClass, script, queryBuilder);
                updateByQueryRequests.add(updateByQueryRequest);
//                try {
//                    BulkByScrollResponse response = elasticsearchRestTemplate.getClient().updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
//                    log.info("表【{}】变动 引起【{}】更新的文档数量：{}", databaseTableColumn.getDatabase() + "." + databaseTableColumn.getTable(), tClass, response.getUpdated());
//                } catch (IOException e) {
//                    log.error("更新elasticsearch数据出错: ", e);
//                    e.printStackTrace();
//                }
            });
        }
        Map<Class, EntityRelatedMapping> entityRelatedMappingMap = synchronizedConfiguration.getRelatedEntityClass(columnKey);
        if (null != entityRelatedMappingMap) {
            entityRelatedMappingMap.forEach((tClass, entityRelatedMapping) -> {
                String relatedValueColumn = entityRelatedMapping.getRelatedValueColumn();
                String relatedTargetColumn = entityRelatedMapping.getRelatedTargetColumn();
                Serializable relateValue = keyValues.get(relatedTargetColumn);
                // 更新nested的脚本
                Map<String, Object> params = new HashMap<>(1);
                params.put(NESTED_KEY, JSONUtil.toBean(JSONUtil.toJsonStr(t), HashMap.class));
                String relateClassUpdateScript = ScriptTemplate.buildScript(entityRelatedMapping.getRelatedField(), NESTED_KEY + "." + NESTED_KEY);
                Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, relateClassUpdateScript, params);
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery(relatedValueColumn, relateValue));
                UpdateByQueryRequest updateByQueryRequest = buildUpdateByRequest(tClass, script, queryBuilder);
                updateByQueryRequests.add(updateByQueryRequest);
            });
        }
        //todo 多线程优化处理
        updateByQueryRequests.stream().forEach(updateByQueryRequest -> {
            try {
                BulkByScrollResponse response = elasticsearchRestTemplate.getClient().updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
                log.info("表【{}】变动 引起【{}】更新，影响的文档数量：{}", databaseTableColumn.getDatabase() + "." + databaseTableColumn.getTable(), JSONUtil.toJsonStr(updateByQueryRequest.indices()), response.getUpdated());
            } catch (IOException e) {
                log.error("更新elasticsearch数据出错: ", e);
                e.printStackTrace();
            }
        });
    }

    private UpdateByQueryRequest buildUpdateByRequest(Class tClass, Script script, QueryBuilder queryBuilder) {
        ElasticsearchPersistentEntity entity = elasticsearchRestTemplate.getPersistentEntityFor(tClass);
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices(entity.getIndexName());
        updateByQueryRequest.setDocTypes(entity.getIndexType());
        updateByQueryRequest.setQuery(queryBuilder);
        updateByQueryRequest.setScript(script);
        updateByQueryRequest.setRefresh(true);
        updateByQueryRequest.setBatchSize(100);
        return updateByQueryRequest;
    }

    private Converter getConverter(Field field, Class valueClass) {
        // 优先取@Converter的转换器
        Converter converter = synchronizedConfiguration.getColumnConverter(field);
        // 从工厂取对应的转换器
        if (null == converter) {
            converter = converterFactory.getConverter(new SourceTargetPair(valueClass, field.getType()));
        }
        return converter;
    }
}
