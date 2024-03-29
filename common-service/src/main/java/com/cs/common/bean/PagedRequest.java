package com.cs.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

/**
 * @ClassName: PagedRequest
 * @Author: CS
 * @Date: 2019/11/5 14:55
 * @Description: 基础分页查询参数
 */
@Data
@ApiModel
@Accessors(chain = true)
public class PagedRequest {

    @Min(0)
    @ApiModelProperty(value = "当前页", example = "1", required = true)
    private Integer currentPage;

    @Min(0)
    @ApiModelProperty(value = "页行数", example = "10", required = true)
    private Integer pageSize;

}
