package com.cs.user.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: UserInfo
 * @Author: CS
 * @Date: 2019/11/5 16:48
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserInfo {

	private Long id;

	private String name;

	private Integer age;

	private String password;

	private Boolean deleted;
}
