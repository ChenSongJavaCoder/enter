package com.cs.workwechat.converter;

import com.cs.workwechat.entity.GroupChatStatistic;
import com.cs.workwechat.pojo.response.GroupChatStatisticResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: CS
 * @Date: 2020/6/3 2:17 下午
 * @Description:
 */
@Component
public class GroupChatStatisticResponseToEntityConverter implements Converter<GroupChatStatisticResponse, List<GroupChatStatistic>> {

    String sys = "sys";

    @Override
    public List<GroupChatStatistic> convert(GroupChatStatisticResponse source) {

        return convert(source, LocalDate.now().minusDays(1));
    }

    public List<GroupChatStatistic> convert(GroupChatStatisticResponse source, LocalDate date) {
        if (Objects.isNull(source)) {
            return Collections.emptyList();
        }

        return source.getItems().stream().map(p -> {
            String owner = p.getOwner();
            GroupChatStatisticResponse.DataBean data = p.getData();
            GroupChatStatistic statistic = new GroupChatStatistic();
            statistic.setOwner(owner)
                    .setChatHasMsg(data.getChat_has_msg())
                    .setChatTotal(data.getChat_total())
                    .setMemberHasMsg(data.getMember_has_msg())
                    .setMemberTotal(data.getMember_total())
                    .setMsgTotal(data.getMsg_total())
                    .setNewChatCnt(data.getNew_chat_cnt())
                    .setNewMemberCnt(data.getNew_member_cnt())
                    .setDefDate(date);
            statistic.setIsDeleted(Boolean.FALSE);
            statistic.setCreatedBy(sys);
            statistic.setCreatedTime(LocalDateTime.now());
            statistic.setUpdatedBy(sys);
            statistic.setUpdatedTime(LocalDateTime.now());
            return statistic;
        }).collect(Collectors.toList());
    }
}
