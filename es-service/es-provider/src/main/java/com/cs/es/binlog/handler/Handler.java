package com.cs.es.binlog.handler;

import com.github.shyiko.mysql.binlog.event.Event;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:20
 * @description: 事件处理器
 */
public interface Handler {

    /**
     * 是否支持
     *
     * @param event
     * @return
     */
    boolean support(Event event);

    /**
     * 处理binlog事件
     *
     * @param event
     */
    void handle(Event event);
}
