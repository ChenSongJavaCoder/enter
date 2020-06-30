package com.cs.workwechat.task;

import com.cs.workwechat.pojo.request.BaseSendMsg;
import com.cs.workwechat.pojo.request.TextSendMsg;
import com.cs.workwechat.service.WorkWechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: CS
 * @Date: 2020/6/18 2:51 下午
 * @Description:
 */
@Slf4j
@Component
public class TestTask {

    @Autowired
    WorkWechatService workWechatService;

    @Scheduled(fixedRate = 60 * 1000 * 60)
    public void sendMsg() {

        BaseSendMsg msg = new TextSendMsg();
        msg.init();

        workWechatService.sendChatMsg(msg);

    }


}
