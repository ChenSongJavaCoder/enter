package com.cs.workwechat.pojo.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:53 下午
 * @Description:
 */
@Data
@ToString
public class GroupChatStatisticResponse extends AbstractResponse {

    /**
     * total : 1
     * next_offset : 1
     * items : [{"owner":"zhangsan","data":{"new_chat_cnt":2,"chat_total":2,"chat_has_msg":0,"new_member_cnt":0,"member_total":6,"member_has_msg":0,"msg_total":0}}]
     */
    /**
     * 当前分页的下一个offset。当next_offset和total相等时，说明已经取完所有
     */
    private int total;
    private int next_offset;
    private List<ItemsBean> items;

    /**
     * 是否拉取完成
     *
     * @return
     */
    public Boolean hasNext() {
        return this.next_offset < this.total;
    }

    @Data
    @ToString
    public static class ItemsBean {
        /**
         * owner : zhangsan
         * data : {"new_chat_cnt":2,"chat_total":2,"chat_has_msg":0,"new_member_cnt":0,"member_total":6,"member_has_msg":0,"msg_total":0}
         */
        private String owner;
        private DataBean data;
    }

    @Data
    public static class DataBean {
        /**
         * new_chat_cnt : 2
         * chat_total : 2
         * chat_has_msg : 0
         * new_member_cnt : 0
         * member_total : 6
         * member_has_msg : 0
         * msg_total : 0
         */
        /**
         * 新增客户群数量
         */
        private int new_chat_cnt;
        /**
         * 截至当天客户群总数量
         */
        private int chat_total;
        /**
         * 截至当天有发过消息的客户群数量
         */
        private int chat_has_msg;
        /**
         * 客户群新增群人数。
         */
        private int new_member_cnt;
        /**
         * 截至当天客户群总人数
         */
        private int member_total;
        /**
         * 截至当天有发过消息的群成员数
         */
        private int member_has_msg;
        /**
         * 截至当天客户群消息总数
         */
        private int msg_total;
    }
}
