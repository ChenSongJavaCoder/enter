package com.cs.demo.chain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: CS
 * @date: 2021/8/2 下午4:37
 * @description:
 */
@Getter
@AllArgsConstructor
public enum ChainOrder {
    A(1),
    B(2),
    C(3),
    D(4),
    ;

    private int order;
}
