package com.cs.task.handler.impl;

import com.cs.task.adaptor.ThirdpartyStrategy;
import com.cs.task.converter.AConverter;
import com.cs.task.enums.TaskType;
import com.cs.task.handler.BizHandler;
import com.cs.task.handler.TaskBaseHandler;
import com.cs.task.pojo.ARequest;
import com.cs.task.pojo.AResponse;
import com.cs.task.pojo.AdaptorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: CS
 * @date: 2021/4/27 上午10:06
 * @description:
 */
@Component
public class ABizHandler extends TaskBaseHandler implements BizHandler<ARequest, AResponse> {

    /**
     * todo 多个第三方策略，使用策略模式确定具体策略
     */
    @Resource
    ThirdpartyStrategy strategy;

    @Autowired
    AConverter aConverter;

    @Override
    public boolean support(TaskType bizType) {
        return TaskType.A.equals(bizType);
    }


    @Override
    public ARequest buildRequest(String... keyword) {
        //todo 构建请求参数
        return aConverter.convert(keyword[0]);
    }


    @Override
    public AdaptorResult<AResponse> bizCall(ARequest request) {
        return strategy.invoke(request);
    }

    /**
     * 整理拿到结果后需要处理的业务逻辑
     * 1、xx
     * 2、xx
     * ……
     *
     * @param adaptorResult
     */
    @Override
    public void bizExecuteAfterCall(AdaptorResult<AResponse> adaptorResult) {

    }

}
