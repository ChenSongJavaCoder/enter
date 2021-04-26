package com.cs.biz.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: CS
 * @date: 2021/4/12 上午10:40
 * @description:
 */
public abstract class AbstractTestService {

    @Autowired
    Invoke invoke;


    public String ann() {
        return invoke.invoke();
    }
}
