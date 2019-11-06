package com.cs.user.pojo;

import com.cs.common.bean.PagedRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Author chenS
 * @Date 2019-11-06 15:08
 * @Description
 **/
@Data
@ApiModel
@Accessors(chain = true)
public class PagedModelRequest extends PagedRequest {

    @NotNull
    private UserInfo userInfo;
}
