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
 * @date: 2021/5/7 下午2:28
 * @description:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
@Document(indexName = "keyword_match", type = "_doc", shards = 1)
@TableMapping(databaseName = "mine", tableName = "keyword_match")
public class EsKeywordMatch {
    @Id
    @Field(type = FieldType.Long)
    @ColumnMapping(columnName = "id")
    private Long id;
    @ColumnMapping(columnName = "keyword")
    @Field(type = FieldType.Keyword)
    private String keyword;
    @ColumnMapping(columnName = "bm")
    @Field(type = FieldType.Keyword)
    private String bm;
    @ColumnMapping(columnName = "create_time")
    @Field(type = FieldType.Date)
    private String createTime;
}
