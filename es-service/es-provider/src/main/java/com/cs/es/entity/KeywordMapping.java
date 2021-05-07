package com.cs.es.entity;

import com.cs.common.mybatis.BaseEntity;
import com.cs.common.mybatis.handler.StringListHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/5/7 下午2:28
 * @description:
 */
@Data
@Accessors(chain = true)
@Table
public class KeywordMapping extends BaseEntity {
    private String keyword;

    @ColumnType(column = "mapping", typeHandler = StringListHandler.class)
    private List<String> mapping;

}
