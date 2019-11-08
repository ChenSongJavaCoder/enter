package com.cs.message.controller;

import com.cs.common.bean.Result;
import com.cs.message.api.RabbitMqApi;
import com.cs.message.pojo.event.EventInfo;
import com.cs.message.service.MessageAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @ClassName: RabbitMqController
 * @Author: CS
 * @Date: 2019/11/8 11:32
 * @Description:
 */
@Slf4j
@Validated
@RestController
public class RabbitMqController implements RabbitMqApi {

	@Autowired
	MessageAdaptor adaptor;

	@Override
	public Result<String> publish(@Valid EventInfo event) {
		return adaptor.send(event);
	}
}
