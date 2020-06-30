package com.cs.workwechat.pojo.request;

import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/6/18 2:23 下午
 * @Description:
 */
@Data
public class BaseSendMsg {

    /**
     * touser : UserID1|UserID2|UserID3
     * toparty : PartyID1|PartyID2
     * totag : TagID1 | TagID2
     * msgtype : text
     * agentid : 1
     * text : {"content":"你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况<\/a>，聪明避开排队。"}
     * safe : 0
     * enable_id_trans : 0
     * enable_duplicate_check : 0
     * duplicate_check_interval : 1800
     */

    private String touser;
    private String toparty;
    private String totag;
    private int agentid;
    private int safe;
    private int enable_id_trans;
    private int enable_duplicate_check;
    private int duplicate_check_interval;


    public BaseSendMsg init() {
        agentid = 1000;
        safe = 0;
        enable_id_trans = 0;
        enable_duplicate_check = 0;
        duplicate_check_interval = 1800;
        return this;
    }

}
