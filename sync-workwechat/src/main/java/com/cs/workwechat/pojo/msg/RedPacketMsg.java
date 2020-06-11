package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class RedPacketMsg extends BaseMsg<RedPacketMsg.RedPacket> {

    private RedPacket redPacket;

    @Data
    public static class RedPacket {

        /**
         * 红包消息类型。1 普通红包、2 拼手气群红包、3 激励群红包。Uint32类型
         */
        private Integer type;

        /**
         * 红包祝福语。String类型
         */
        private String wish;

        /**
         * 红包总个数。Uint32类型
         */
        private Integer totalcnt;

        /**
         * 红包总金额。Uint32类型，单位为分。
         */
        private Integer totalamount;
    }
}
