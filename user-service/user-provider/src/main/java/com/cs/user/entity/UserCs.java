package com.cs.user.entity;

import com.cs.common.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: UserCs
 * @Author: CS
 * @Date: 2019/11/2 10:48
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
@EqualsAndHashCode
public class UserCs extends BaseEntity {

	private String name;

	private Integer age;

	private String password;
}
