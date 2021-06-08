package com.cs.es.binlog.config;

import com.cs.es.binlog.bean.DatabaseTablePair;
import com.cs.es.binlog.converter.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keosn
 * @date 2019/3/26 14:33
 */
@Component
@Slf4j
public class SynchronizedConfiguration {

    /**
     * 保存需要同步表结构的表; NOTE: DatabaseTablePair 区分库名;
     */
    private Map<DatabaseTablePair, List<Class>> relations = new ConcurrentHashMap<>();

    /**
     * 字段映射关系
     * Map key documentField value cloumn
     */
    private Map<DocumentTableMapping, Map<String, String>> columnMappings = new ConcurrentHashMap<>();

    /**
     * 字段关联关系（正向取数）
     */

    private Map<Class, Map<String, ColumnRelatedMapping>> relatedColumnMappingForClass = new ConcurrentHashMap<>();

    /**
     * 字段关联关系（逆向取数）
     */

    private Map<String, Map<Class, ColumnRelatedMapping>> columnRelateClassMappingForClass = new ConcurrentHashMap<>();

    /**
     * 字段关联实体的关系（正向取数）
     */
    private Map<Class, Map<String, EntityRelatedMapping>> entityRelatedMappingForClass = new ConcurrentHashMap<>();

    /**
     * 字段关联实体的关系（逆向取数）
     */
    private Map<String, Map<Class, EntityRelatedMapping>> entityRelatedClassMapping = new ConcurrentHashMap<>();


    /**
     * 关联表&关联字段记录（同步更新）
     */
    private Map<DatabaseTablePair, Map<Class, List<ColumnRelatedMapping>>> relatedColumnMapping = new ConcurrentHashMap<>();

    /**
     * 属性关联对象关联关系(同步更新)
     */
    private Map<DatabaseTablePair, Map<Class, List<EntityRelatedMapping>>> entityRelatedMapping = new ConcurrentHashMap<>();

    /**
     * 属性关联对象关联关系(同步更新)
     */
    private Map<DatabaseTablePair, Map<Class, List<EntityRelatedMapping>>> beEntityRelatedMapping = new ConcurrentHashMap<>();

    /**
     * 缓存字段转换器
     */
    private Map<Field, Converter> fieldConverterCache = new ConcurrentHashMap<>();

    public void addTable(DocumentTableMapping documentTableMapping) {
        DatabaseTablePair databaseTablePair = new DatabaseTablePair(documentTableMapping.getDatabase(), documentTableMapping.getTable());

        if (!relations.containsKey(databaseTablePair)) {
            relations.put(databaseTablePair, new ArrayList<>());
        }

        relations.get(databaseTablePair).add(documentTableMapping.getDocumentClass());
    }

    public List<Class> getMappingDocumentClass(DatabaseTablePair databaseTablePair) {
        return this.relations.get(databaseTablePair);
    }

    /**
     * 判断是否需要构建tableId缓存
     *
     * @param databaseTablePair
     * @return
     */
    public boolean containsDatabaseTablePair(DatabaseTablePair databaseTablePair) {
        return this.relations.containsKey(databaseTablePair);
    }

    /**
     * 添加字段映射关系
     *
     * @param documentTableMapping
     * @param documentField
     * @param tableColumn
     */
    public void addColumnMapping(DocumentTableMapping documentTableMapping, String documentField, String tableColumn) {
        if (!this.columnMappings.containsKey(documentTableMapping)) {
            this.columnMappings.put(documentTableMapping, new HashMap<>());
        }
        this.columnMappings.get(documentTableMapping).put(documentField, tableColumn);
    }

    public Map<String, String> getColumnMapping(DocumentTableMapping documentTableMapping) {
        return this.columnMappings.get(documentTableMapping);
    }

    public Map<String, ColumnRelatedMapping> getRelatedColumnMapping(Class clazz) {
        return this.relatedColumnMappingForClass.get(clazz);
    }

    public Map<String, EntityRelatedMapping> getEntityRelatedColumnMapping(Class clazz) {
        return this.entityRelatedMappingForClass.get(clazz);
    }

    /**
     * 添加字段关联关系
     * todo 添加被关联关系
     *
     * @param databaseTablePair
     * @param columnRelated
     */
    public void addColumnRelated(DatabaseTablePair databaseTablePair, Class clazz, ColumnRelatedMapping columnRelated) {
        if (!this.relatedColumnMapping.containsKey(databaseTablePair)) {
            Map<Class, List<ColumnRelatedMapping>> clazzColumns = new HashMap<>();
            this.relatedColumnMapping.put(databaseTablePair, clazzColumns);
        }
        if (!this.relatedColumnMapping.get(databaseTablePair).containsKey(clazz)) {
            this.relatedColumnMapping.get(databaseTablePair).put(clazz, new ArrayList<>());
        }
        this.relatedColumnMapping.get(databaseTablePair).get(clazz).add(columnRelated);

        String columnRelateClassKey = columnKey(columnRelated.getRelatedDatabase(), columnRelated.getRelatedTable(), columnRelated.getTargetColumn());
        if (!this.columnRelateClassMappingForClass.containsKey(columnRelateClassKey)) {
            Map<Class, ColumnRelatedMapping> columnClasses = new HashMap<>();
            this.columnRelateClassMappingForClass.put(columnRelateClassKey, columnClasses);
        }
        columnRelateClassMappingForClass.get(columnRelateClassKey).put(clazz, columnRelated);

        if (!this.relatedColumnMappingForClass.containsKey(clazz)) {
            this.relatedColumnMappingForClass.put(clazz, new HashMap<>());
        }
        this.relatedColumnMappingForClass.get(clazz).put(columnRelated.getFieldName(), columnRelated);
    }

    public String columnKey(String database, String table, String column) {
        return database + table + column;
    }

    public Map<Class, List<ColumnRelatedMapping>> getRelatedClassMapping(DatabaseTablePair databaseTablePair) {
        return this.relatedColumnMapping.get(databaseTablePair);
    }

    public Map<Class, ColumnRelatedMapping> getColumnRelatedClassMapping(String column) {
        return this.columnRelateClassMappingForClass.get(column);
    }

    /**
     * 构建DatabaseTablePair和被关联的Table Row模型
     *
     * @param clazz
     * @param entityRelatedMapping
     */
    public void addEntityRelated(Class clazz, EntityRelatedMapping entityRelatedMapping) {
        // 正向关联映射
        if (CollectionUtils.isEmpty(this.entityRelatedMappingForClass.get(clazz))) {
            this.entityRelatedMappingForClass.put(clazz, new HashMap<>());
        }
        this.entityRelatedMappingForClass.get(clazz).put(entityRelatedMapping.getRelatedField(), entityRelatedMapping);

        // 逆向关联映射
        String columnKey = columnKey(entityRelatedMapping.getDatabase(), entityRelatedMapping.getTableName(), entityRelatedMapping.getRelatedTargetColumn());
        if (CollectionUtils.isEmpty(this.entityRelatedClassMapping.get(columnKey))) {
            this.entityRelatedClassMapping.put(columnKey, new HashMap<>());
        }
        this.entityRelatedClassMapping.get(columnKey).put(entityRelatedMapping.getRelatedClazz(), entityRelatedMapping);


        DatabaseTablePair databaseTablePair = new DatabaseTablePair(entityRelatedMapping.getDatabase(), entityRelatedMapping.getTableName());
        if (CollectionUtils.isEmpty(this.entityRelatedMapping.get(databaseTablePair))) {
            this.entityRelatedMapping.put(databaseTablePair, new HashMap<>());
        }

        if (CollectionUtils.isEmpty(this.entityRelatedMapping.get(databaseTablePair).get(clazz))) {
            this.entityRelatedMapping.get(databaseTablePair).put(clazz, new ArrayList<>());
        }
        this.entityRelatedMapping.get(databaseTablePair).get(clazz).add(entityRelatedMapping);

        // 逆向
        if (CollectionUtils.isEmpty(this.beEntityRelatedMapping.get(databaseTablePair))) {
            this.beEntityRelatedMapping.put(databaseTablePair, new HashMap<>());
        }
        if (CollectionUtils.isEmpty(this.beEntityRelatedMapping.get(databaseTablePair).get(entityRelatedMapping.getTargetClazz()))) {
            this.beEntityRelatedMapping.get(databaseTablePair).put(entityRelatedMapping.getTargetClazz(), new ArrayList<>());
        }
        this.beEntityRelatedMapping.get(databaseTablePair).get(entityRelatedMapping.getTargetClazz()).add(entityRelatedMapping);

    }

    /**
     * 获取DatabaseTablePair和被关联的Table Row
     *
     * @param databaseTablePair
     * @return
     */
    public Map<Class, List<EntityRelatedMapping>> getRelatedEntityList(DatabaseTablePair databaseTablePair) {
        return this.entityRelatedMapping.get(databaseTablePair);
    }

    public Map<Class, EntityRelatedMapping> getRelatedEntityClass(String column) {
        return this.entityRelatedClassMapping.get(column);
    }

    public Map<Class, List<EntityRelatedMapping>> getBeRelatedEntityList(DatabaseTablePair databaseTablePair) {
        return this.beEntityRelatedMapping.get(databaseTablePair);
    }

    public void addColumnConverter(Field field, Class<? extends Converter> converter) {
        try {
            this.fieldConverterCache.put(field, converter.newInstance());
        } catch (InstantiationException e) {
            log.error("Instantiation Converter error", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccess with Converter", e);
        }
    }

    public Converter getColumnConverter(Field field) {
        return this.fieldConverterCache.get(field);
    }
}
