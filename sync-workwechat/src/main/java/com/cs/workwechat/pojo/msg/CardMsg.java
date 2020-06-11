package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class CardMsg extends BaseMsg<CardMsg.Card> {

    private Card card;

    @Data
    public static class Card {

        /**
         * 名片所有者的id，同一公司是userid，不同公司是external_userid。String类型
         */
        private String userid;

        /**
         * 名片所有者所在的公司名称。String类型
         */
        private String corpname;
    }
}
