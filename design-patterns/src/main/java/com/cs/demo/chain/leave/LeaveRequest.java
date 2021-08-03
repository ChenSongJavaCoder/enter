package com.cs.demo.chain.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author: CS
 * @date: 2021/8/3 下午2:14
 * @description:
 */
@Data
@ToString
@AllArgsConstructor
public class LeaveRequest {

    private int leaveDays;

    private String name;
}
