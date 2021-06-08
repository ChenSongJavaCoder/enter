package com.cs.es.binlog.builder;

import cn.hutool.json.JSONUtil;
import com.cs.es.binlog.bean.DatabaseTableColumn;
import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.EntityRelatedMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/6/8 上午11:49
 * @description:
 */
@Slf4j
@Component
public class AsynchronousRelatedDocumentUpdater {

    public static final String NESTED_KEY = "params";

    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    UpdateByQueryBuilder updateByQueryBuilder;


    public <T> void doUpdate(T t, List<String> beingRelatedColumn, DocumentTableMapping documentTableMapping, Map<String, Serializable> keyValues) {
        for (String column : beingRelatedColumn) {
            new UpdateRelatedDocument(t, new DatabaseTableColumn(documentTableMapping.getDatabase(), documentTableMapping.getTable(), column), keyValues).start();
        }
    }


    private class UpdateRelatedDocument<T> extends Thread {
        private T t;
        private DatabaseTableColumn databaseTableColumn;
        private Map<String, Serializable> keyValues;

        public UpdateRelatedDocument(T t, DatabaseTableColumn databaseTableColumn, Map<String, Serializable> keyValues) {
            super(UpdateRelatedDocument.class.getSimpleName());
            this.t = t;
            this.databaseTableColumn = databaseTableColumn;
            this.keyValues = keyValues;
        }

        @Override
        public void run() {
            updateRelatedDocument(t, databaseTableColumn, keyValues);
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
                    log.info("关联字段类更新脚本：{}", relateClassUpdateScript);

                    Script script = new Script(relateClassUpdateScript);
                    TermQueryBuilder queryBuilder = QueryBuilders.termQuery(targetRelateColumn, relateValue);
                    UpdateByQueryRequest updateByQueryRequest = updateByQueryBuilder.buildUpdateByRequest(tClass, script, queryBuilder);
                    updateByQueryRequests.add(updateByQueryRequest);
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
                    log.info("关联实体类更新脚本：{}", relateClassUpdateScript);
                    Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, relateClassUpdateScript, params);
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery(relatedValueColumn, relateValue));
                    UpdateByQueryRequest updateByQueryRequest = updateByQueryBuilder.buildUpdateByRequest(tClass, script, queryBuilder);
                    updateByQueryRequests.add(updateByQueryRequest);
                });
            }
            //todo 多线程优化处理,可以使用异步更新方式调用
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
    }
}
