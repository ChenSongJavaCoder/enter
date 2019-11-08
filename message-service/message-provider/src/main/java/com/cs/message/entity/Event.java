package com.cs.message.entity;

import com.cs.common.mybatis.BaseEntity;
import com.cs.message.pojo.event.EventType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName: Event
 * @Author: CS
 * @Date: 2019/11/8 13:51
 * @Description:
 */
@Data
@Accessors(chain = true)
public class Event extends BaseEntity {

	/**
	 * 事件id 使用uuid
	 */
	private String eventId;

	/**
	 * 事件类型
	 */
	private EventType eventType;

	/**
	 * 事件的参数
	 */
	private String eventParam;

	/**
	 * 事件的发生时间
	 */
	private LocalDateTime eventTime;
}
