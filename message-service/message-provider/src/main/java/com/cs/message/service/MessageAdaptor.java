package com.cs.message.service;

import com.cs.common.bean.Result;
import com.cs.message.client.UserStreamClient;
import com.cs.message.pojo.event.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MessageAdaptor
 * @Author: CS
 * @Date: 2019/11/8 13:48
 * @Description: 消息适配
 */
@Component
public class MessageAdaptor {

	@Autowired
	UserStreamClient userStreamClient;

	public Result<String> send(EventInfo event) {
		boolean result;
		switch (event.getEventType()) {
			case CREATE_USER:
				result = userStreamClient.createStoreChannel().send(MessageBuilder.withPayload(event.getEventParam()).build());
				if (result) {
					return Result.success().build();
				}
			default:
				return Result.failure().build();
		}
	}
}
