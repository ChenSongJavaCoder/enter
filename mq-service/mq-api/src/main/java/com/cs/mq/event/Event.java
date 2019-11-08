package com.cs.mq.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Author chenS
 * @Date 2019-11-08 09:48
 * @Description TODO 将mq所有的消息事件进行持久化存储
 **/
@Data
@ApiModel
@Accessors(chain = true)
public class Event {

    private Long id;

    /**
     * 事件id 使用uuid
     */
    @NotNull
    @ApiModelProperty(value = "事件id 使用uuid")
    private String eventId;

    /**
     * 事件类型
     */
    @NotNull
    @ApiModelProperty(value = "事件类型")
    private EventType eventType;

    /**
     * 事件的参数
     */
    @NotNull
    @ApiModelProperty(value = "事件的参数")
    private String eventParam;

    /**
     * 事件的发生时间
     */
    @NotNull
    @ApiModelProperty(value = "事件的发生时间")
    private LocalDateTime eventTime;


}
