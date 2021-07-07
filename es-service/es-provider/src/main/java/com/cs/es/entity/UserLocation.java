package com.cs.es.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author: CS
 * @date: 2021/6/10 下午3:22
 * @description:
 */
@Data
@Accessors(chain = true)
@Table
public class UserLocation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer storeId;
}
