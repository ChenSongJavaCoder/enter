package com.cs.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName: PagedRequest
 * @Author: CS
 * @Date: 2019/11/5 14:55
 * @Description: 基础分页返回参数
 */
@Data
@ApiModel
@Accessors(chain = true)
public class PagedResult<T> {

	@ApiModelProperty(value = "数据集", required = true)
	private List<T> rows;

	@ApiModelProperty(value = "总条数", example = "100", required = true)
	private long total;

}
