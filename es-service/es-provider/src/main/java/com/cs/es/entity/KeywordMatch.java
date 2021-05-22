package com.cs.es.entity;

import com.cs.common.mybatis.BaseEntity;
import com.cs.es.binlog.annotation.TableMapping;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @author: CS
 * @date: 2021/5/7 下午2:28
 * @description:
 */
@Data
@Accessors(chain = true)
@Table
public class KeywordMatch extends BaseEntity {
    private String keyword;
    private String bm;
}
