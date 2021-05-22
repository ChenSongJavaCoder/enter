package com.cs.es.binlog.scan;

import cn.hutool.core.util.ClassUtil;
import com.cs.es.binlog.annotation.ColumnMapping;
import com.cs.es.binlog.annotation.*;
import com.cs.es.binlog.config.*;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author: CS
 * @date: 2021/5/8 下午4:29
 * @description: 初始化加载数据库映射类
 */
@Slf4j
@Component
public class TableMappingProcessor {

    @Autowired
    SynchronizedConfiguration synchronizedConfiguration;

    @Value("${elasticsearch.document.path:com.cs.es.document}")
    private String documentPath;

    /**
     * 数据库-javaBean映射
     * todo rename
     */
    private DocumentTableMapping documentTableMapping;

    @PostConstruct
    public void init() {
        // 该方法可扫描子路径下的类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(documentPath, TableMapping.class);
        classes.forEach(clazz -> {
            TableMapping annotation = AnnotationUtils.findAnnotation(clazz, TableMapping.class);
            // 若表名未定义取类名驼峰转下划线作为表名
            String tableName = StringUtils.hasText(annotation.tableName()) ? annotation.tableName() : CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
            documentTableMapping = new DocumentTableMapping(clazz, annotation.databaseName(), tableName);
            synchronizedConfiguration.addTable(documentTableMapping);
            log.info("数据库java类名:{},表名:{}", clazz.getSimpleName(), tableName);

            //todo、 es index相关

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                ColumnMapping mappingAnnotation = field.getAnnotation(ColumnMapping.class);
                ColumnRelated relatedColumnAnnotation = field.getAnnotation(ColumnRelated.class);
                EntityRelated relatedEntityAnnotation = field.getAnnotation(EntityRelated.class);
                Converter converterAnnotation = field.getAnnotation(Converter.class);

                if (null != converterAnnotation) {
                    synchronizedConfiguration.addColumnConverter(field, converterAnnotation.value());
                }

                if (null != mappingAnnotation) {
                    synchronizedConfiguration.addColumnMapping(documentTableMapping, mappingAnnotation.columnName(), field.getName());
                } else if (null != relatedColumnAnnotation) {
                    DatabaseTablePair databaseTablePair = new DatabaseTablePair(relatedColumnAnnotation.databaseName(), relatedColumnAnnotation.tableName());

                    ColumnRelatedMapping columnRelatedMapping = new ColumnRelatedMapping(field.getName(), relatedColumnAnnotation.databaseName(), relatedColumnAnnotation.tableName(), relatedColumnAnnotation.relatedColumn(), relatedColumnAnnotation.relatedTargetColumn(), relatedColumnAnnotation.targetColumn());

                    synchronizedConfiguration.addColumnRelated(databaseTablePair, clazz, columnRelatedMapping);
                } else if (null != relatedEntityAnnotation) {
                    EntityRelatedMapping entityRelatedMapping = new EntityRelatedMapping(
                            relatedEntityAnnotation.databaseName(),
                            relatedEntityAnnotation.tableName(),
                            relatedEntityAnnotation.targetField(),
                            relatedEntityAnnotation.valueFiled(),
                            field.getName());

                    synchronizedConfiguration.addEntityRelated(clazz, entityRelatedMapping);
                } else {
                    log.warn("Document [{}] field[{}] with any mapping or related configuration! The value of this field will not be set!", clazz.getSimpleName(), field.getName());
                }
            }
        });

    }
}
