package com.cs.demo.chain;

import com.cs.common.model.Chain;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/8/2 下午3:12
 * @description:
 */
@Component
public class ChainNodeUserC implements Chain<User, User> {

    @Override
    public void handle(User req, User resp) {
        resp.setAge(req.getAge() + 5);
    }

    @Override
    public boolean support(User user) {
        return true;
    }

    @Override
    public Chain<User, User> successor() {
        return null;
    }

    @Override
    public Integer order() {
        return ChainOrder.C.getOrder();
    }
}
