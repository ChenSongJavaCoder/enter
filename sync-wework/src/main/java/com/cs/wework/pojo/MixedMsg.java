package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import com.cs.wework.pojo.enums.MsgType;
import lombok.Data;

import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class MixedMsg extends BaseMsg<MixedMsg.Mixed> {

    /**
     * mixed内包含一个item数组，其中每个元素由type与content组成，type和content均为String类型。JSON解析content后即可获取对应type类型的消息内容。
     */
    private Mixed mixed;

    @Data
    public static class Mixed {

        private List<Item> items;
    }

    @Data
    public static class Item {

        private MsgType type;

        /**
         * 此处的content对应各种消息的content，格式不同，可与type一起解析
         */
        private String content;
    }
}
