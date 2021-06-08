package com.cs.es.binlog.builder;

import com.cs.es.binlog.bean.ColumnModifyBean;
import com.cs.es.binlog.bean.SourceTargetPair;
import com.cs.es.binlog.converter.Converter;
import com.cs.es.binlog.converter.ConverterFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: CS
 * @date: 2021/6/5 下午10:15
 * @description:
 */
@Slf4j
@Component
public class UpdateByQueryBuilder {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    ConverterFactory converterFactory;


    /**
     * 被关联实体更新时引起关联实体的更新
     *
     * @param documentClass
     * @param documentField
     * @param clazz
     * @param modifyColumns
     * @param fieldColumnMap
     * @param queryBuilder
     * @return
     */
    public UpdateByQueryRequest buildNestedUpdateByRequest(Class documentClass, String documentField, Class clazz, List<ColumnModifyBean> modifyColumns, Map<String, String> fieldColumnMap, QueryBuilder queryBuilder) {
        StringBuffer scriptString = new StringBuffer();
        for (ColumnModifyBean columnModifyBean : modifyColumns) {
            Optional<Map.Entry<String, String>> fieldColumnEntry = fieldColumnMap.entrySet().stream().filter(f -> f.getValue().equals(columnModifyBean.getColumn())).findAny();
            if (!fieldColumnEntry.isPresent()) {
                continue;
            }
            Serializable newColumnValue = columnModifyBean.getNewValue();
            // 获取需要更新的值
            Object newFieldValue;
            Field field = null;
            Converter converter;
            try {
                field = clazz.getDeclaredField(fieldColumnEntry.get().getKey());
            } catch (NoSuchFieldException e) {
                log.error("获取类字段异常：", e);
            }
            log.info("column：{} modified, document：{}.{} need sync update!：", columnModifyBean.getColumn(), documentClass.getSimpleName(), documentField);

            converter = converterFactory.getConverter(new SourceTargetPair(newColumnValue.getClass(), field.getType()));
            if (null != converter) {
                newFieldValue = converter.convert(newColumnValue);
            } else {
                newFieldValue = newColumnValue;
            }

            // 编写script脚本，注意脚本newFieldValue的属性问题
            String updateCodeTemplate = ScriptTemplate.buildScript(documentField + "." + field.getName(), newFieldValue);
            scriptString.append(updateCodeTemplate);
        }
        Script script = new Script(scriptString.toString());
        log.info("buildNestedUpdateByRequest更新脚本为：{}", scriptString.toString());
        return buildUpdateByRequest(documentClass, script, queryBuilder);
    }


    /**
     * @param clazz          document class
     * @param modifyColumns
     * @param fieldColumnMap
     * @return
     */
    public UpdateByQueryRequest buildUpdateByRequest(Class clazz, List<ColumnModifyBean> modifyColumns, Map<String, String> fieldColumnMap, QueryBuilder queryBuilder) {
        StringBuffer scriptString = new StringBuffer();
        for (ColumnModifyBean columnModifyBean : modifyColumns) {
            Optional<Map.Entry<String, String>> fieldColumnEntry = fieldColumnMap.entrySet().stream().filter(f -> f.getValue().equals(columnModifyBean.getColumn())).findAny();
            if (!fieldColumnEntry.isPresent()) {
                continue;
            }
            Serializable newColumnValue = columnModifyBean.getNewValue();
            // 获取需要更新的值
            Object newFieldValue;
            Field field = null;
            Converter converter;
            try {
                field = clazz.getDeclaredField(fieldColumnEntry.get().getKey());
            } catch (NoSuchFieldException e) {
                log.error("获取类字段异常：", e);
            }
            log.info("column：{} modified, document：{}.{} need sync update!：", columnModifyBean.getColumn(), clazz.getSimpleName(), field.getName());

            converter = converterFactory.getConverter(new SourceTargetPair(newColumnValue.getClass(), field.getType()));
            if (null != converter) {
                newFieldValue = converter.convert(newColumnValue);
            } else {
                newFieldValue = newColumnValue;
            }

            // 编写script脚本，注意脚本newFieldValue的属性问题
            String updateCodeTemplate = ScriptTemplate.buildScript(field.getName(), newFieldValue);
            scriptString.append(updateCodeTemplate);
        }
        Script script = new Script(scriptString.toString());
        log.info("buildUpdateByRequest更新脚本为：{}", scriptString.toString());
        return buildUpdateByRequest(clazz, script, queryBuilder);
    }


    public UpdateByQueryRequest buildUpdateByRequest(Class tClass, Script script, QueryBuilder queryBuilder) {
        ElasticsearchPersistentEntity entity = elasticsearchRestTemplate.getPersistentEntityFor(tClass);
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices(entity.getIndexName());
        updateByQueryRequest.setDocTypes(entity.getIndexType());
        updateByQueryRequest.setQuery(queryBuilder);
        updateByQueryRequest.setScript(script);
        updateByQueryRequest.setRefresh(true);
        updateByQueryRequest.setBatchSize(100);
        updateByQueryRequest.setAbortOnVersionConflict(false);
        return updateByQueryRequest;
    }
}