package com.cs.es.binlog.builder;

import com.cs.es.binlog.bean.Null;
import com.cs.es.binlog.bean.SourceTargetPair;
import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.EntityRelatedMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.converter.Converter;
import com.cs.es.binlog.converter.ConverterFactory;
import com.cs.es.document.EsUserInfo;
import com.cs.es.document.EsUserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: CS
 * @date: 2021/5/22 下午9:42
 * @description: 1、构建表->document直接映射对象
 * 2、处理关联字段、对象 正向关联取值关系 使用{@link DocumentMappingBuilder#LOCAL_FIELD_PREFIX}  {@link EsUserRole#getUserId()} 和 {@link EsUserRole#getUsername()}
 * 3、处理关联字段、对象 逆向关联取值关系 {@link EsUserInfo#getRoleId()}
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
    AsynchronousRelatedDocumentUpdater asynchronousRelatedDocumentUpdater;


    /**
     * 构建实例对象
     *
     * @param documentTableMapping 文档数据库表映射
     * @param keyValues            数据
     * @param <T>                  实例对象
     * @return
     */
    public <T> T build(DocumentTableMapping documentTableMapping, Map<String, Serializable> keyValues, boolean isRootObject) {
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
                    // 数据库映射字段
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
                    // 关联字段映射 正向关联
                    if (null != relatedColumnMappings && relatedColumnMappings.keySet().contains(field.getName())) {
                        log.info("Got related mapping column {}", field.getName());
                        ColumnRelatedMapping relatedColumnMapping = relatedColumnMappings.get(field.getName());
                        // 获取关联字段值 类似'join on'中的'on'字段值
                        Serializable relatedValue;
                        String relatedCol = relatedColumnMapping.getRelatedColumn();
                        if (relatedCol.startsWith(LOCAL_FIELD_PREFIX)) {
                            relatedValue = currentFieldCache.get(relatedCol.replaceFirst(LOCAL_FIELD_PREFIX, StringUtils.EMPTY));
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
                    }
                    // 关联实体类 正向关联, action: 通过isRootObject不进行内部对象的创建
                    if (isRootObject && null != entityRelatedColumnMappings && entityRelatedColumnMappings.keySet().contains(field.getName())) {
                        log.info("Got related entity column {}", field.getName());
                        EntityRelatedMapping entityRelatedColumnMapping = entityRelatedColumnMappings.get(field.getName());
                        String relatedCol = entityRelatedColumnMapping.getRelatedValueColumn();
                        if (relatedCol.startsWith(LOCAL_FIELD_PREFIX)) {
                            Serializable relatedValue = keyValues.get(relatedCol);
                            // 脏数据引起数据错乱,因为id的值可能会串！
                            Map<String, Serializable> cacheValue = relatedValueGetter.getRowValue(entityRelatedColumnMapping, relatedValue);
                            // 注意形成死循环 action: 通过isRootObject不进行内部对象的创建
                            if (null != cacheValue && isRootObject) {
                                Object relatedEntity = build(new DocumentTableMapping(field.getType(), entityRelatedColumnMapping.getDatabase(), entityRelatedColumnMapping.getTableName()), cacheValue, false);
                                field.set(t, relatedEntity);
                            }
                        }
                    }
                    // 通过isRootObject判断是否需要依赖关系的更新
                    if (isRootObject && Objects.nonNull(column)) {
                        beingRelatedColumn.add(column);
                    }
                }
                asynchronousRelatedDocumentUpdater.doUpdate(t, beingRelatedColumn, documentTableMapping, keyValues);
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

    /**
     * 获取转换器
     *
     * @param field
     * @param valueClass
     * @return
     */
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
