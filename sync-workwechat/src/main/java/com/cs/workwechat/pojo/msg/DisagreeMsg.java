package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class DisagreeMsg extends BaseMsg<DisagreeMsg.Disagree> {

    private Disagree disagree;

    @Data
    public static class Disagree {

        /**
         * 同意/不同意协议者的userid，外部企业默认为external_userid。String类型
         */
        private String userid;

        /**
         * 同意/不同意协议的时间，utc时间，ms单位。
         */
        private Long disagree_time;
    }
}
