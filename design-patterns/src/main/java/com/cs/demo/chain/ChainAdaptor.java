package com.cs.demo.chain;

import com.cs.demo.chain.leave.LeaveRequest;
import com.cs.demo.chain.leave.LeaveResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Autowired
    List<Chain<LeaveRequest, LeaveResponse>> leaveChains;

    /**
     * 获取队首的链式处理器
     *
     * @return
     */
    public Optional<Chain<LeaveRequest, LeaveResponse>> getHeadChain() {
        // 使用sort方式进行排序
        return leaveChains.stream().sorted().findFirst();
    }

    @PostConstruct
    public void test() {
//        User u = new User("name", 1);
//        Stream.of(u).forEach(e -> getHeadChain().ifPresent(p -> p.chain(u, u)));
//        System.out.println(u.toString());

        LeaveRequest leaveRequest = new LeaveRequest(30, "cs");
        LeaveResponse leaveResponse = new LeaveResponse();
        getHeadChain().ifPresent(e -> e.chain(leaveRequest, leaveResponse));

        System.out.println(leaveResponse);

    }


}
