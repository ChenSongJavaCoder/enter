package com.cs.wework.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @Author: CS
 * @Date: 2020/3/21 4:17 下午
 * @Description:
 */
@Data
@Accessors(chain = true)
@Table(name = "enterprise_wechat_external_user")
public class Customer extends BaseEntity {

    private String userId;

    private String name;

    private Integer type;

    private Integer gender;

    private String unionId;

    private String corpName;

    private String corpFullName;

}
