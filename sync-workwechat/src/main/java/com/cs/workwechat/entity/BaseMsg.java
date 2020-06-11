package com.cs.workwechat.entity;

import com.cs.workwechat.handler.JsonHandler;
import com.cs.workwechat.handler.StringListHandler;
import com.cs.workwechat.pojo.enums.MsgType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/3/16 6:19 下午
 * @Description:
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "work_wechat_msg")
public class BaseMsg<T> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 消息序列号
     */
    private Long seq;

    /**
     * 消息类型
     */
    @Column(name = "msg_type")
    private MsgType msgtype;

    /**
     * 消息id，消息的唯一标识，企业可以使用此字段进行消息去重。String类型
     */
    @Column(name = "msg_id")
    private String msgid;

    /**
     * 消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型。String类型
     */
    private String action;
    /**
     * 消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid。String类型
     */
    @ColumnType(column = "from_a")
    private String from;

    /**
     * 消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组，内容为string类型
     */
    @ColumnType(column = "to_list", typeHandler = StringListHandler.class)
    private List<String> tolist;

    /**
     * 群聊消息的群id。如果是单聊则为空。String类型
     */
    @Column(name = "room_id")
    private String roomid;

    /**
     * 消息发送时间戳，utc时间，ms单位。
     */
    @Column(name = "msg_time")
    private Long msgtime;

    @Column(name = "oss_path")
    private String ossPath;

    @ColumnType(column = "content", typeHandler = JsonHandler.class)
    private T content;


}
