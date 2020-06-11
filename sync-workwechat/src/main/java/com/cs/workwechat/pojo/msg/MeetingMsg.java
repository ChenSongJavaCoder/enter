package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class MeetingMsg extends BaseMsg<MeetingMsg.Meeting> {

    private Meeting meeting;

    @Data
    public static class Meeting {
        /**
         * 会议主题。String类型
         */
        private String topic;

        /**
         * 会议开始时间。Utc时间
         */
        private Long starttime;
        /**
         * 会议结束时间。Utc时间
         */
        private Long endtime;

        /**
         * 会议地址。String类型
         */
        private String address;

        /**
         * 会议备注。String类型
         */
        private String remarks;
        /**
         * 会议消息类型。101发起会议邀请消息、102处理会议邀请消息。Uint32类型
         */
        private Integer meetingtype;
        /**
         * 会议id。方便将发起、处理消息进行对照。uint64类型
         */
        private Long meetingid;
        /**
         * 会议邀请处理状态。1 参加会议、2 拒绝会议、3 待定。Uint32类型
         */
        private Integer status;
    }
}
