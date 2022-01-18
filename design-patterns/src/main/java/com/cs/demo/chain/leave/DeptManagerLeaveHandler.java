package com.cs.demo.chain.leave;

import com.cs.common.model.Chain;
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
            currentNode = new LeaveResponse.Node(managerName, true, "同意");
            nodes.add(currentNode);
        }
    }

    /**
     * 大于1天需要经过主管
     *
     * @param leaveRequest
     * @return
     */
    @Override
    public boolean support(LeaveRequest leaveRequest) {
        return leaveRequest.getLeaveDays() > MIN;
    }

    @Override
    public Chain<LeaveRequest, LeaveResponse> successor() {
        return gManagerLeaveHandler;
    }

    @Override
    public Integer order() {
        return ChainOrder.B.getOrder();
    }
}
