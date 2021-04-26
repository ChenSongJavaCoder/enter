package com.cs.biz.service;

import org.springframework.stereotype.Service;

/**
 * @author: CS
 * @date: 2021/4/12 上午10:26
 * @description:
 */
@Service
public class TestServiceImpl extends AbstractTestService implements TestService {


    @Override
    public String testAnn() {
        return ann();
    }


}
