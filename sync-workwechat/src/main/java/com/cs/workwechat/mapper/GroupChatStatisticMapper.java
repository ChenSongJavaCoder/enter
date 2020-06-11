package com.cs.workwechat.mapper;

import com.cs.common.mybatis.BaseMapper;
import com.cs.workwechat.entity.GroupChatStatistic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @Author: CS
 * @Date: 2020/6/3 2:02 下午
 * @Description:
 */
@Repository
public interface GroupChatStatisticMapper extends BaseMapper<GroupChatStatistic> {

    @Select("select def_date from work_wechat_group_chat_statistic where def_date = #{date} limit 1")
    LocalDate selectDateData(@Param(value = "date") LocalDate date);
}
