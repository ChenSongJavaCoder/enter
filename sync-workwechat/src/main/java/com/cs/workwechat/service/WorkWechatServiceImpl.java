package com.cs.workwechat.service;

import com.cs.common.util.LocalDateTimeUtil;
import com.cs.workwechat.constants.WorkWechatOpenApi;
import com.cs.workwechat.converter.GroupChatStatisticResponseToEntityConverter;
import com.cs.workwechat.entity.GroupChatStatistic;
import com.cs.workwechat.mapper.GroupChatStatisticMapper;
import com.cs.workwechat.pojo.request.BaseSendMsg;
import com.cs.workwechat.pojo.request.GroupChatStatisticRequest;
import com.cs.workwechat.pojo.response.GroupChatStatisticResponse;
import com.cs.workwechat.pojo.response.SendMsgResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/6/2 5:11 下午
 * @Description:
 */
@Slf4j
@Service
public class WorkWechatServiceImpl extends AbstractWorkWechatService implements WorkWechatService {

    @Autowired
    GroupChatStatisticMapper groupChatStatisticMapper;

    @Autowired
    GroupChatStatisticResponseToEntityConverter groupChatStatisticResponseToEntityConverter;

    @Override
    public GroupChatStatisticResponse getGroupChatStatistic(GroupChatStatisticRequest request) {
        String url = String.format(WorkWechatOpenApi.GROUP_CHAT_STATISTIC, getAccessToken());
        GroupChatStatisticResponse response = postForResponse(url, request, GroupChatStatisticResponse.class);
        return response;
    }

    @Override
    public void pullAllGroupChatStatistic(LocalDate date) {
        if (date.equals(groupChatStatisticMapper.selectDateData(date))) {
            return;
        }
        GroupChatStatisticRequest request = new GroupChatStatisticRequest();
        Long startTime = LocalDateTimeUtil.timeToEpochSecond(date.atTime(LocalTime.MIN));
        request.setDay_begin_time(startTime);
        List<GroupChatStatistic> data = Lists.newArrayList();
        GroupChatStatisticResponse temp;
        while (true) {
            temp = getGroupChatStatistic(request);
            data.addAll(groupChatStatisticResponseToEntityConverter.convert(temp, date));
            log.info("数据日期:{} 数据大小:{} 是否结束:{}", date.format(DateTimeFormatter.ISO_DATE), data.size(), temp.hasNext());
            if (!temp.hasNext()) {
                break;
            }
            request.setOffset(temp.getNext_offset());
        }
        if (!CollectionUtils.isEmpty(data)) {
            groupChatStatisticMapper.insertList(data);
            dingTalkRobotClient.sendTextMessage("企业微信同步群统计数据: " + data.size() + "条" + "数据日期：" + date.format(DateTimeFormatter.ISO_DATE));
        }
    }

    @Override
    public void sendChatMsg(BaseSendMsg msg) {
        String url = String.format(WorkWechatOpenApi.SEND_CHAT_MSG, getAccessToken());
        SendMsgResponse response = postForResponse(url, msg, SendMsgResponse.class);
    }
}
