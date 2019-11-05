package com.cs.common.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: KeyValBean
 * @Author: CS
 * @Date: 2019/11/5 16:16
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class KeyValBean {
	private String key;
	private String value;
}
