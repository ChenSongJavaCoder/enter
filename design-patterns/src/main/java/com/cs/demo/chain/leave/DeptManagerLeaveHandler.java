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
public class DeptManagerLeaveHandler extends AbstractLeaveHandler {

    @Autowired
    GManagerLeaveHandler gManagerLeaveHandler;

    public DeptManagerLeaveHandler() {
        managerName = "部门主管";
    }

    @Override
    public void handle(LeaveRequest req, LeaveResponse resp) {
        List<LeaveResponse.Node> nodes = resp.getNodes();
        LeaveResponse.Node currentNode;
        // 前一个领导审批通过
        if (nodes.get(nodes.size() - 1).isSuccess()) {
            currentNode = new LeaveResponse.Node(managerName, false, "不同意");
            nodes.add(currentNode);
        }
    }

    @Override
    public boolean support(LeaveRequest leaveRequest) {
        return leaveRequest.getLeaveDays() >= MIDDLE;
    }

    @Override
    public Chain<LeaveRequest, LeaveResponse> successor() {
        return gManagerLeaveHandler;
    }

    @Override
    public int order() {
        return ChainOrder.B.getOrder();
    }
}
