package com.cs.demo.chain.leave;

import com.cs.demo.chain.Chain;
import com.cs.demo.chain.ChainOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/8/3 下午2:17
 * @description:
 */
@Component
public class DirectLeaderLeaveHandler extends AbstractLeaveHandler {

    @Autowired
    DeptManagerLeaveHandler deptManagerLeaveHandler;


    public DirectLeaderLeaveHandler() {
        managerName = "直接主管";
    }


    @Override
    public void handle(LeaveRequest req, LeaveResponse resp) {
        resp.getNodes().add(new LeaveResponse.Node(managerName, true, "同意"));
    }

    @Override
    public boolean support(LeaveRequest leaveRequest) {
        return leaveRequest.getLeaveDays() >= MIN;
    }

    @Override
    public Chain<LeaveRequest, LeaveResponse> successor() {
        return deptManagerLeaveHandler;
    }

    @Override
    public int order() {
        return ChainOrder.A.getOrder();
    }
}
