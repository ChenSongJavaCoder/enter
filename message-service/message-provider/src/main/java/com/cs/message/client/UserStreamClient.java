package com.cs.message.client;

import com.cs.message.channel.UserChannel;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


/**
 * @ClassName: UserStreamClient
 * @Author: CS
 * @Date: 2019/11/8 13:58
 * @Description:
 */
public interface UserStreamClient {

	/**
	 * 创建用户
	 *
	 * @return
	 */
	@Output(UserChannel.CREATE_USER)
	MessageChannel createStoreChannel();
}
