package com.cs.message.service;

import com.cs.common.bean.Result;
import com.cs.message.client.UserStreamClient;
import com.cs.message.entity.Event;
import com.cs.message.mapper.EventMapper;
import com.cs.message.pojo.event.EventInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: MessageAdaptor
 * @Author: CS
 * @Date: 2019/11/8 13:48
 * @Description: 消息适配
 */
@Component
public class MessageAdaptor {

    @Autowired
    EventMapper eventMapper;

    @Autowired
    UserStreamClient userStreamClient;

    @Transactional(rollbackFor = Exception.class)
    public Result<String> send(EventInfo event) {
        Event e = new Event();
        BeanUtils.copyProperties(event, e);
        e.setEventType(event.getEventType().name());
//        eventMapper.insertSelective(e);

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
