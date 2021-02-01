package com.cs.miniprogram.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: CS
 * @date: 2020/11/27 4:57 下午
 * @description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class CreateOrUpdateUserInvoiceTitleRequest extends UserInvoiceTitleInfo {
}
