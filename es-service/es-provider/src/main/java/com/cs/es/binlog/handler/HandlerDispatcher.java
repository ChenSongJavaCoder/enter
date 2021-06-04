package com.cs.es.binlog.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:25
 * @description: 处理器分发器
 */
@Component
public class HandlerDispatcher {

    @Autowired
    List<Handler> handlerList;

    public Optional<Handler> getHandler(Event event) {
        return handlerList.stream().filter(f -> f.support(event)).findAny();
    }

}
