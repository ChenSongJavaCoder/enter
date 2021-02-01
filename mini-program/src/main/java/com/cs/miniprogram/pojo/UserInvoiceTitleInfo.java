package com.cs.miniprogram.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author: CS
 * @date: 2020/11/27 4:49 下午
 * @description: 用户发票抬头信息
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserInvoiceTitleInfo {
    @ApiModelProperty(value = "id 新增时不传值，修改时传值")
    private String id;
    @NotBlank
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    @ApiModelProperty(value = "抬头类型：1：企业 2：个人/其他", required = true)
    private Integer titleType;
    @ApiModelProperty(value = "开票抬头", required = true)
    private String buyersTaxName;
    @ApiModelProperty(value = "税号", required = true)
    private String nsrsbh;
    @ApiModelProperty(value = "地址电话", required = false)
    private String dzdh;
    @ApiModelProperty(value = "开户行及账号", required = false)
    private String openBankAndAccount;
    @ApiModelProperty(value = "手机号码", required = false)
    private String phoneNumber;
    @ApiModelProperty(value = "邮箱", required = false)
    private String email;
    @ApiModelProperty(value = "是否默认：0否 1是", required = true)
    private Integer isDefault;
}
