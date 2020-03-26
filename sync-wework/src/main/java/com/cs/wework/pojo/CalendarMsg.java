package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;

import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class CalendarMsg extends BaseMsg<CalendarMsg.Calendar> {

    private Calendar calendar;

    @Data
    public static class Calendar {

        /**
         * 日程主题
         */
        private String title;

        /**
         * 日程组织者
         */
        private String creatorname;

        /**
         * 日程参与人
         */
        private List<String> attendeename;

        /**
         * 日程开始时间
         */
        private Long starttime;
        /**
         * 日程结束时间
         */
        private Long endtime;

        /**
         * 日程地点
         */
        private String place;

        /**
         * 日程备注
         */
        private String remarks;
    }
}
