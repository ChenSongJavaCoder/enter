package com.cs.analyzer.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @Author: CS
 * @Date: 2020/2/14 9:20 下午
 * @Description:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "keyword")
public class Keyword {

    private String keyword;
}
