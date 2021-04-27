package com.cs.task.handler;


import com.cs.task.enums.TaskType;
import com.cs.task.pojo.AdaptorBaseRequest;
import com.cs.task.pojo.AdaptorBaseResponse;
import com.cs.task.pojo.AdaptorResult;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:26
 * @description: 业务处理
 */
public interface BizHandler<Req extends AdaptorBaseRequest, Resp extends AdaptorBaseResponse> {

    /**
     * 是否支持该业务类型
     *
     * @param bizType
     * @return
     */
    default boolean support(TaskType bizType) {
        return false;
    }

    /**
     * 组装请求参数
     *
     * @param keyword
     * @return
     */
    Req buildRequest(String... keyword);

    /**
     * 业务调用
     *
     * @param request
     * @return
     */
    AdaptorResult<Resp> bizCall(Req request);


    /**
     * 执行成功后的业务逻辑
     *
     * @param adaptorResult
     */
    void bizExecuteAfterCall(AdaptorResult<Resp> adaptorResult);

}
