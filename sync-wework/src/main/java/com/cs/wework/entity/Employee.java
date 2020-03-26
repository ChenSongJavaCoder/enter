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
@Table(name = "enterprise_wechat_member")
public class Employee extends BaseEntity {

    private String userId;

    private String name;

    private String mobile;

    private Integer gender;

    private Integer enable;

    private Integer status;

    private String qrCode;
}
