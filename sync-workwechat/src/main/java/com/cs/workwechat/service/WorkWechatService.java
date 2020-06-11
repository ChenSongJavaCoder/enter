package com.cs.workwechat.service;

import com.cs.workwechat.pojo.request.GroupChatStatisticRequest;
import com.cs.workwechat.pojo.response.GroupChatStatisticResponse;

import java.time.LocalDate;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:09 下午
 * @Description: 企业微信相关接口服务
 */
public interface WorkWechatService {

    /**
     * 外部群统计数据
     *
     * @param request
     * @return
     */
    GroupChatStatisticResponse getGroupChatStatistic(GroupChatStatisticRequest request);

    /**
     * 拉取某一天第全部外部群统计信息
     *
     * @param date
     */
    void pullAllGroupChatStatistic(LocalDate date);
}
