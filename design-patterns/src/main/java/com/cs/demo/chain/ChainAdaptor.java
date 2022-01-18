package com.cs.demo.chain;

import com.cs.common.model.Chain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author: CS
 * @date: 2021/8/2 下午3:58
 * @description:
 */
@Component
public class ChainAdaptor {

    /**
     * demo数据
     */
    @Autowired
    List<Chain<User, User>> userChains;
    /**
     * 获取队首的链式处理器
     *
     * @return
     */
    public Optional<Chain<User, User>> getHeadChain() {
        // 使用sort方式进行排序
        return userChains.stream().sorted().findFirst();
    }

    //    @PostConstruct
    public void test() {
//        User u = new User("name", 1);
//        Stream.of(u).forEach(e -> getHeadChain().ifPresent(p -> p.chain(u, u)));
//        System.out.println(u.toString());

    }


}
