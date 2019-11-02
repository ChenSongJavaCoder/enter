package com.cs.user.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: UserDto
 * @Author: CS
 * @Date: 2019/11/2 16:08
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserDto {

	private Long id;

	private String name;

	private Integer age;

	private String password;
}
