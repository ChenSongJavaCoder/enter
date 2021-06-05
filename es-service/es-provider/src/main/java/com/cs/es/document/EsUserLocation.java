package com.cs.es.document;

import com.cs.es.binlog.annotation.ColumnMapping;
import com.cs.es.binlog.annotation.TableMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

/**
 * @author: CS
 * @date: 2021/6/4 下午2:27
 * @description:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
@Document(indexName = "es_user_location", type = "_doc", shards = 1)
@TableMapping(databaseName = "mine", tableName = "user_location")
public class EsUserLocation implements EsDocument {
    @Id
    @Field(type = FieldType.Long)
    @ColumnMapping(columnName = "id")
    private Long id;

    @ColumnMapping(columnName = "user_id")
    @Field(type = FieldType.Integer)
    private Integer userId;

    @ColumnMapping(columnName = "store_id")
    @Field(type = FieldType.Integer)
    private Integer storeId;
}
