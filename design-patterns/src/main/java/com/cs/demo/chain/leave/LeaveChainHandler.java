package com.cs.demo.chain.leave;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/8/3 下午10:02
 * @description: 请假申请处理器
 */
@Slf4j
@Component
public class LeaveChainHandler {

    @Autowired
    List<AbstractLeaveHandler> abstractLeaveHandlers;

    /**
     * 提供这样的一个handle方法，直接查看处理结果
     *
     * @param leaveRequest
     * @return
     */
    public LeaveResponse handle(LeaveRequest leaveRequest) {
        LeaveResponse leaveResponse = new LeaveResponse();
        abstractLeaveHandlers.stream().sorted().findFirst().ifPresent(e -> e.chain(leaveRequest, leaveResponse));
        return leaveResponse;
    }


    @PostConstruct
    public void test() {
        LeaveRequest leaveRequest = new LeaveRequest(30, "cs");
        LeaveResponse response = handle(leaveRequest);
        log.info(JSONUtil.toJsonPrettyStr(response));
    }
}
