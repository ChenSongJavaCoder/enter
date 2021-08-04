package com.cs.demo.chain.leave;

import com.cs.demo.chain.Chain;

/**
 * @author: CS
 * @date: 2021/8/3 下午2:16
 * @description: 规则：
 * 请假小于1天：直接主管审批处理
 * 请假大于1天小于3天：直接主管、部门经理审批处理
 * 请假大于3天小于30天：直接主管、部门经理、总经理审批处理
 * 请假大于30天：系统拒绝审批通过处理
 */
public abstract class AbstractLeaveHandler implements Chain<LeaveRequest, LeaveResponse> {

    /**
     * 直接主管审批处理的请假天数
     */
    protected int MIN = 1;
    /**
     * 部门经理处理的请假天数
     */
    protected int MIDDLE = 3;
    /**
     * 总经理处理的请假天数
     */
    protected int MAX = 30;
    /**
     * 管理员名称
     */
    protected String managerName;
    /**
     * 执行意见
     */
    protected String message;
}
