package com.cs.task.adaptor;

import com.cs.task.pojo.ARequest;
import com.cs.task.pojo.AResponse;
import com.cs.task.pojo.AdaptorResult;

/**
 * @author: CS
 * @date: 2021/4/27 下午3:46
 * @description: 第三方策略
 */
public interface ThirdpartyStrategy {

    AdaptorResult<AResponse> invoke(ARequest request);
}
