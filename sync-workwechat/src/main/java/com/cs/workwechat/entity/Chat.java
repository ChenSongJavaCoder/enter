package com.cs.workwechat.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @Author: CS
 * @Date: 2020/3/21 4:51 下午
 * @Description: 什么脑残操作
 */
@Data
@Accessors(chain = true)
@Table(name = "enterprise_wechat_msg")
public class Chat extends BaseEntity {

    private String corpId;
    private String msgId;
    private Long msgSeq;
    private Integer source;
    private Boolean isAuto;
    private Long userId;
    private Long empId;
    private String fromId;
    private String toId;
    private String content;
    private String contentObj;
    private String msgType;
    private LocalDateTime msgTime;
    private String userNickName;
    private Boolean isGroup;
    private Boolean isCancel;
    private String roomId;
    private String ossPath;
}
