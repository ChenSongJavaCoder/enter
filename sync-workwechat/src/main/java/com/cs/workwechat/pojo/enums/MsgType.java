package com.cs.workwechat.pojo.enums;

import com.cs.workwechat.pojo.msg.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: CS
 * @Date: 2020/3/12 5:13 下午
 * @Description: 消息类型
 */
@Getter
@AllArgsConstructor
public enum MsgType {
    text("文本", TextMsg.class),
    image("图片", ImageMsg.class),
    agree("同意会话聊天内容", AgreeMsg.class),
    disagree("不同意会话聊天内容", AgreeMsg.class),
    revoke("撤回消息", RevokeMsg.class),
    // 媒体文件
    voice("语音", VoiceMsg.class),
    video("视频", VideoMsg.class),
    card("名片", CardMsg.class),
    location("位置", LocationMsg.class),
    emotion("表情", EmotionMsg.class),
    file("文件", FileMsg.class),
    link("链接", LinkMsg.class),
    weapp("小程序", WeappMsg.class),
    chatrecord("会话记录消息，item子对象", Object.class),
    todo("待办消息", TodoMsg.class),
    vote("投票消息", Object.class),
    collect("填表消息", Object.class),
    redpacket("红包消息", RedPacketMsg.class),
    meeting("会议邀请消息", MeetingMsg.class),
    //    switch("切换企业日志"),
    docmsg("在线文档消息", DocMsg.class),
    markdown("MarkDown格式消息", MarkdownMsg.class),
    news("图文消息", NewsMsg.class),
    calendar("日程消息", CalendarMsg.class),
    mixed("混合消息", MixedMsg.class),
    meeting_voice_call("音频存档消息", Object.class),
    voip_doc_share("音频共享文档消息", Object.class),
    ;

    private String desc;
    private Class clazz;


}
