package com.cs.message.api;

import com.cs.common.bean.Result;
import com.cs.message.pojo.event.EventInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author chenS
 * @Date 2019-11-08 10:32
 * @Description 消息队列，可以说所有的事件消息（包括短信邮件钉钉消息等等推送至第三方的消息）都可以使用此方式进行消息推送，后续逻辑的处理
 **/
public interface RabbitMqApi {

	@ApiOperation("发布消息")
	@PostMapping(value = "publish", produces = MediaType.APPLICATION_JSON_VALUE)
	Result<String> publish(@RequestBody @Valid EventInfo event);


}
