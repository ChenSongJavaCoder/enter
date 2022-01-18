package com.cs.common.eventbus;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.ApplicationContext;

/**
 * spring eventBus
 *
 * @author chensong
 */
public class EventBus {

    private static final Object LOCK = new Object();
    public static EventBus instance;

    private EventBus() {
    }

    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new EventBus();
            }
        }
        return instance;
    }

    /**
     * 发布事件
     * 配合使用
     *
     * @param event
     * @see org.springframework.context.event.EventListener
     */
    public void publish(Object event) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        if (context != null) {
            context.publishEvent(event);
        }
    }
}
