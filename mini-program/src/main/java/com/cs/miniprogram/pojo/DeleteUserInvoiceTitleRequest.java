package com.cs.miniprogram.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author: CS
 * @date: 2020/11/27 5:00 下午
 * @description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class DeleteUserInvoiceTitleRequest {
    @NotBlank
    @ApiModelProperty(value = "抬头id", required = true)
    private String id;
}
