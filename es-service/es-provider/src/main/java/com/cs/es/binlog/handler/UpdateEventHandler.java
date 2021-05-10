package com.cs.es.binlog.handler;

import com.cs.es.binlog.mysql.TableMetadataBuilder;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description:
 */
@Slf4j
@Component
public class UpdateEventHandler implements Handler {

    @Autowired
    TableMetadataBuilder tableMetadataBuilder;

    @Override
    public boolean support(Event event) {
        return EventType.isUpdate(event.getHeader().getEventType());
    }

    @Override
    public void handle(Event event) {
        log.info("监听到更新事件：{}", event.toString());
    }
}
