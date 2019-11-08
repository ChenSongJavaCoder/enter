package com.cs.message.pojo.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenS
 * @Date 2019-11-08 09:48
 * @Description 描述事件和定义消息队列
 **/
@Getter
@AllArgsConstructor
public enum EventType {

	CREATE_USER("创建新用户"),
    ;

	private String desc;
}
