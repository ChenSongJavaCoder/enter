package com.cs.es.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @ClassName: Groups
 * @Author: CS
 * @Date: 2019/10/8 17:49
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class Groups {

    @ApiModelProperty
    private String field;

    @ApiModelProperty
    private Map<String, Long> group;


}
