package com.cs.workwechat.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @Author: CS
 * @Date: 2020/6/3 1:57 下午
 * @Description:
 */
@Data
@Accessors(chain = true)
@Table(name = "work_wechat_group_chat_statistic")
public class GroupChatStatistic extends BaseEntity {

    /**
     * 群主
     */
    private String owner;
    /**
     * 新增客户群数量
     */
    private Integer newChatCnt;
    /**
     * 截至当天客户群总数量
     */
    private Integer chatTotal;
    /**
     * 截至当天有发过消息的客户群数量
     */
    private Integer chatHasMsg;
    /**
     * 客户群新增群人数。
     */
    private Integer newMemberCnt;
    /**
     * 截至当天客户群总人数
     */
    private Integer memberTotal;
    /**
     * 截至当天有发过消息的群成员数
     */
    private Integer memberHasMsg;
    /**
     * 截至当天客户群消息总数
     */
    private Integer msgTotal;
    /**
     * 数据日期
     */
    private LocalDate defDate;
}
