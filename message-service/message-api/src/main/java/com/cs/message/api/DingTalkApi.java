package com.cs.message.api;

import com.cs.common.bean.Result;
import com.cs.message.pojo.event.DingTalkMsg;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: CS
 * @Date: 2020/6/12 10:51 上午
 * @Description:
 */
public interface DingTalkApi {

    /**
     * 发送钉钉消息
     *
     * @param dingTalkMsg
     * @return
     */
    Result<Boolean> sendMessage(@RequestBody @Valid DingTalkMsg dingTalkMsg);
}
