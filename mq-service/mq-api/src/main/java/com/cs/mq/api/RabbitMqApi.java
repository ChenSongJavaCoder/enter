package com.cs.mq.api;

import com.cs.common.bean.Result;
import com.cs.mq.event.Event;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author chenS
 * @Date 2019-11-08 10:32
 * @Description
 **/
public interface RabbitMqApi {

    @PostMapping(value = "publish", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<String> publish(@RequestBody @Valid Event event, BindingResult bindingResult);


}
