package com.cs.demo.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/8/2 下午3:12
 * @description:
 */
@Component
public class ChainNodeUserB implements Chain<User, User> {


    @Autowired
    ChainNodeUserC chainNodeUserC;


    @Override
    public void handle(User req, User resp) {
        resp.setAge(req.getAge() + 3);
    }

    @Override
    public boolean support(User user) {
        return true;
    }

    @Override
    public Chain<User, User> successor() {
        return chainNodeUserC;
    }

    @Override
    public Integer order() {
        return ChainOrder.B.getOrder();
    }
}
