package com.cs.biz.service;

import com.cs.biz.aop.TestAnn;
import com.cs.biz.util.TestAnnUtil;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/4/12 上午11:33
 * @description:
 */
@Component
public class Invoke {

    @TestAnn
    public String invoke() {
        return TestAnnUtil.testAnn();
    }
}
