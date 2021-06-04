package com.cs.es.binlog.builder;

import com.cs.es.binlog.bean.Null;
import com.cs.es.binlog.bean.SourceTargetPair;
import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.DocumentTableMapping;
import com.cs.es.binlog.config.EntityRelatedMapping;
import com.cs.es.binlog.config.SynchronizedConfiguration;
import com.cs.es.binlog.converter.Converter;
import com.cs.es.binlog.converter.ConverterFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/5/22 下午9:42
 * @description: 构建document实例
 */
@Slf4j
@Component
public class DocumentMappingBuilder {

    public static final String LOCAL_FIELD_PREFIX = "@";
    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;
    @Autowired
    RelatedValueGetter relatedValueGetter;
    @Autowired
    ConverterFactory converterFactory;

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
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    // 映射字段
                    if (columnMappings.keySet().contains(field.getName())) {
                        log.trace("Got  mapping column {}", field.getName());
                        Serializable value = keyValues.get(columnMappings.get(field.getName()));
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
                    if (null != entityRelatedColumnMappings && entityRelatedColumnMappings.keySet().contains(field.getName())) {
                        EntityRelatedMapping entityRelatedMapping = entityRelatedColumnMappings.get(field.getName());
                        Object relatedValue = currentFieldCache.get(entityRelatedMapping.getValueField());
                        Map<String, Serializable> value = relatedValueGetter.getRowValue(entityRelatedMapping, relatedValue);
                        field.set(t, value);
                    }
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
