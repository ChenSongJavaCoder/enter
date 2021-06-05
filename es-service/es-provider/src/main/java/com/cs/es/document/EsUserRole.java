package com.cs.es.document;

import com.cs.es.binlog.annotation.ColumnMapping;
import com.cs.es.binlog.annotation.ColumnRelated;
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
@Document(indexName = "es_user_role", type = "_doc", shards = 1)
@TableMapping(databaseName = "mine", tableName = "user_role")
public class EsUserRole implements EsDocument {
    @Id
    @Field(type = FieldType.Long)
    @ColumnMapping(columnName = "id")
    private Long id;

    @ColumnMapping(columnName = "user_id")
    @Field(type = FieldType.Integer)
    private Integer userId;

    @ColumnRelated(databaseName = "mine", tableName = "user", relatedColumn = "user_id", relatedTargetColumn = "id", targetColumn = "username")
    @Field(type = FieldType.Keyword)
    private String username;

    @ColumnRelated(databaseName = "mine", tableName = "user", relatedColumn = "user_id", relatedTargetColumn = "id", targetColumn = "nickname")
    @Field(type = FieldType.Keyword)
    private String nickname;

    @ColumnRelated(databaseName = "mine", tableName = "user", relatedColumn = "user_id", relatedTargetColumn = "id", targetColumn = "age")
    @Field(type = FieldType.Integer)
    private Integer userAge;

    @ColumnMapping(columnName = "role_id")
    @Field(type = FieldType.Integer)
    private Integer roleId;
}
