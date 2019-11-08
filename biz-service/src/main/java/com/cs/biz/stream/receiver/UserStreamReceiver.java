package com.cs.biz.stream.receiver;

import com.cs.message.channel.UserChannel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.http.client.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @ClassName: UserStreamReceiver
 * @Author: CS
 * @Date: 2019/11/8 15:41
 * @Description:
 */
@Slf4j
@Component
public class UserStreamReceiver {

	ObjectMapper objectMapper = new ObjectMapper();

	@StreamListener(UserChannel.CREATE_USER)
	public void createUserReceiver(@Payload UserInfo userInfo) throws JsonProcessingException {
		log.info("收到来自user-service：create_user 的消息");
		log.info(objectMapper.writeValueAsString(userInfo));
	}
}
