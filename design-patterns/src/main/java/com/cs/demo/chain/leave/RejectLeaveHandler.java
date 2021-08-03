package com.cs.demo.chain.leave;

import com.cs.demo.chain.Chain;
import com.cs.demo.chain.ChainOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: CS
 * @date: 2021/8/3 下午2:17
 * @description:
 */
@Component
public class RejectLeaveHandler extends AbstractLeaveHandler {

    @Autowired
    DirectLeaderLeaveHandler directLeaderLeaveHandler;

    public RejectLeaveHandler() {
        managerName = "系统管理员";
    }

    @Override
    public void handle(LeaveRequest req, LeaveResponse resp) {
        List<LeaveResponse.Node> nodes = resp.getNodes();
        LeaveResponse.Node currentNode = new LeaveResponse.Node(managerName, false, "拒绝通过");
        nodes.add(currentNode);
    }

    @Override
    public boolean support(LeaveRequest leaveRequest) {
        return leaveRequest.getLeaveDays() >= MAX;
    }

    @Override
    public Chain<LeaveRequest, LeaveResponse> successor() {
        return null;
    }

    @Override
    public int order() {
        return ChainOrder.D.getOrder();
    }
}
