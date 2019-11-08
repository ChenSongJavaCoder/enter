package com.cs.biz.stream;

import com.cs.message.channel.UserChannel;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName: StreamClient
 * @Author: CS
 * @Date: 2019/11/8 15:39
 * @Description:
 */
public interface StreamClient {

	@Input(UserChannel.CREATE_USER)
	SubscribableChannel createUser();


}
