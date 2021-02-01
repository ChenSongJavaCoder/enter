package com.cs.miniprogram.pojo;

import com.cs.miniprogram.base.PagedRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @author: CS
 * @date: 2020/11/27 5:39 下午
 * @description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class PagedUserInvoiceRequest extends PagedRequest {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true, example = "13")
    @NotBlank
    private String userId;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号", required = true, example = "13")
    private String mobile;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    /**
     * 发票号码/销方名称：最多输入100个字符；发票号码精准搜索，销方名称模糊搜索。
     */
    @ApiModelProperty("发票号码/销方名称")
    private String keyword;

    /**
     * 开票状态：单选；开票中 / 开票完成 / 开票失败 / 验签失败 / 解析异常
     */
    @ApiModelProperty("开票状态：单选；开票中 / 开票完成 / 开票失败 / 验签失败 / 解析异常")
    private Integer invoiceProcess;

    /**
     * 发票种类：单选；电子普票 / 电子专票 / 普通发票 / 专用发票
     */
    @ApiModelProperty("发票种类：单选；电子普票 / 电子专票 / 普通发票 / 专用发票")
    private Integer invoiceType;

    /**
     * 发票状态：单选；正常 / 红冲 / 作废
     */
    @ApiModelProperty("发票状态：单选；正常 / 红冲 / 作废")
    private Integer invoiceState;
}
