package com.cs.demo.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/8/2 下午3:12
 * @description:
 */
@Component
public class ChainNodeUserA implements Chain<User, User> {

    @Autowired
    ChainNodeUserB chainNodeB;


    @Override
    public void handle(User req, User resp) {
        resp.setAge(req.getAge() + 1);
    }

    @Override
    public boolean support(User user) {
        return true;
    }

    @Override
    public Chain<User, User> successor() {
        return chainNodeB;
    }

    @Override
    public int order() {
        return ChainOrder.A.getOrder();
    }
}
