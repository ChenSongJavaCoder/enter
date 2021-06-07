package com.cs.es.document;

import com.cs.es.binlog.annotation.ColumnMapping;
import com.cs.es.binlog.annotation.ColumnRelated;
import com.cs.es.binlog.annotation.EntityRelated;
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
@Document(indexName = "es_user_info", type = "_doc", shards = 1)
@TableMapping(databaseName = "mine", tableName = "user")
public class EsUserInfo implements EsDocument {
    @Id
    @Field(type = FieldType.Long)
    @ColumnMapping(columnName = "id")
    private Long id;

    /**
     * 用户名 可用作登陆，具有唯一性（eg：工号，手机号）
     */
    @ColumnMapping(columnName = "username")
    @Field(type = FieldType.Keyword)
    private String username;

    /**
     * 姓名 昵称 用作日常展示 eg：霸王龙
     */
    @ColumnMapping(columnName = "nickname")
    @Field(type = FieldType.Keyword)
    private String nickname;

    @ColumnMapping(columnName = "age")
    @Field(type = FieldType.Integer)
    private Integer age;

    @ColumnMapping(columnName = "password")
    @Field(type = FieldType.Keyword)
    private String password;

    @ColumnRelated(databaseName = "mine", tableName = "user_role", relatedColumn = "id", relatedTargetColumn = "user_id", targetColumn = "role_id")
    @Field(type = FieldType.Integer)
    private Integer roleId;

    @EntityRelated(databaseName = "mine", tableName = "user_location", relatedValueColumn = "id", relatedTargetColumn = "user_id")
    @Field(type = FieldType.Nested)
    private EsUserLocation userLocation;
}
