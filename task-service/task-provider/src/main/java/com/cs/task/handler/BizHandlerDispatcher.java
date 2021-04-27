package com.cs.task.handler;

import com.cs.task.enums.TaskType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:26
 * @description: 处理器分发策略
 */
@Component
public class BizHandlerDispatcher {
    @Resource
    private List<BizHandler> handlers;

    public Optional<BizHandler> getSupportedHandler(TaskType bizType) {
        return handlers.stream().filter(h -> h.support(bizType)).findAny();
    }

}
