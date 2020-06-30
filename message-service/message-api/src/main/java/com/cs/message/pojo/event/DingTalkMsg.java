package com.cs.message.pojo.event;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/6/12 11:03 上午
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class DingTalkMsg {

    /**
     * 内容
     */
    private String content;

    /**
     * 目的地
     */
    private DingTalkDestination destination;

}
