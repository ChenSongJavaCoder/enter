package com.cs.miniprogram.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: CS
 * @Date: 2020/10/22 16:02
 * @Description:
 */
@Data
@ApiModel
public class PagedRequest {
    /**
     * 当前页
     */
    @NotNull
    @ApiModelProperty(name = "当前页", required = true, example = "1")
    private Integer currentPage;

    /**
     * 页大小
     */
    @NotNull
    @ApiModelProperty(name = "页大小", required = true, example = "10")
    private Integer pageSize;
}
