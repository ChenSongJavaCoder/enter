package com.cs.message.pojo.event;

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
public class EventInfo<T> {
	/**
	 * 事件id 使用uuid
	 */
	@NotNull
	@ApiModelProperty(value = "事件id 使用uuid", required = true)
	private String eventId;

	/**
	 * 事件类型
	 */
	@NotNull
	@ApiModelProperty(value = "事件类型", required = true)
	private EventType eventType;

	/**
	 * 事件的参数
	 */
	@NotNull
	@ApiModelProperty(value = "事件的参数", required = true)
	private T eventParam;

	/**
	 * 事件的发生时间
	 */
	@NotNull
	@ApiModelProperty(value = "事件的发生时间", required = true)
	private LocalDateTime eventTime;


}
