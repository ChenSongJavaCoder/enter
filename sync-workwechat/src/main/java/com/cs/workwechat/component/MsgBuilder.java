package com.cs.workwechat.component;

import com.cs.workwechat.entity.BaseMsg;
import com.cs.workwechat.pojo.enums.MediaType;
import com.cs.workwechat.pojo.enums.MsgType;
import com.cs.workwechat.pojo.msg.*;
import com.cs.workwechat.service.OssService;
import com.cs.workwechat.util.SDKUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * @Author: CS
 * @Date: 2020/3/19 2:44 下午
 * @Description:
 */
@Slf4j
@Component
public class MsgBuilder {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OssService ossService;

    /**
     * 构建baseMsg
     *
     * @param msgType
     * @param chatData
     * @param seq
     * @return
     * @throws Exception
     */
    public BaseMsg buildBaseMsg(MsgType msgType, String chatData, Long seq) throws Exception {
        switch (msgType) {
            case text:
                TextMsg textMsg = objectMapper.readValue(chatData, TextMsg.class);
                textMsg.setContent(textMsg.getText());
                textMsg.setSeq(seq);
                return textMsg;
            case image:
                ImageMsg imageMsg = objectMapper.readValue(chatData, ImageMsg.class);
                imageMsg.setContent(imageMsg.getImage());
                imageMsg.setSeq(seq);
                uploadOss(imageMsg.getImage().getSdkfileid(), MediaType.image, imageMsg);
                return imageMsg;
            case emotion:
                EmotionMsg emotionMsg = objectMapper.readValue(chatData, EmotionMsg.class);
                emotionMsg.setContent(emotionMsg.getEmotion());
                emotionMsg.setSeq(seq);
                uploadOss(emotionMsg.getEmotion().getSdkfileid(), MediaType.emotion, emotionMsg);
                return emotionMsg;
            case file:
                FileMsg fileMsg = objectMapper.readValue(chatData, FileMsg.class);
                fileMsg.setContent(fileMsg.getFile());
                fileMsg.setSeq(seq);
                uploadOss(fileMsg.getFile().getSdkfileid(), MediaType.file, fileMsg);
                return fileMsg;
            case video:
                VideoMsg videoMsg = objectMapper.readValue(chatData, VideoMsg.class);
                videoMsg.setContent(videoMsg.getVideo());
                videoMsg.setSeq(seq);
                uploadOss(videoMsg.getVideo().getSdkfileid(), MediaType.video, videoMsg);
                return videoMsg;
            case voice:
                VoiceMsg voiceMsg = objectMapper.readValue(chatData, VoiceMsg.class);
                voiceMsg.setContent(voiceMsg.getVoice());
                voiceMsg.setSeq(seq);
                uploadOss(voiceMsg.getVoice().getSdkfileid(), MediaType.voice, voiceMsg);
                return voiceMsg;
            case meeting_voice_call:
                MeetingVoiceCallMsg meetingVoiceCallMsg = objectMapper.readValue(chatData, MeetingVoiceCallMsg.class);
                meetingVoiceCallMsg.setContent(meetingVoiceCallMsg.getMeeting_voice_call());
                meetingVoiceCallMsg.setSeq(seq);
                uploadOss(meetingVoiceCallMsg.getMeeting_voice_call().getSdkfileid(), MediaType.voice, meetingVoiceCallMsg);
                return meetingVoiceCallMsg;
            case voip_doc_share:
                VoipDocShareMsg voipDocShareMsg = objectMapper.readValue(chatData, VoipDocShareMsg.class);
                voipDocShareMsg.setContent(voipDocShareMsg.getVoip_doc_share());
                voipDocShareMsg.setSeq(seq);
                uploadOss(voipDocShareMsg.getVoip_doc_share().getSdkfileid(), MediaType.voice, voipDocShareMsg);
                return voipDocShareMsg;
            case weapp:
                WeappMsg weappMsg = objectMapper.readValue(chatData, WeappMsg.class);
                weappMsg.setContent(weappMsg.getWeapp());
                weappMsg.setSeq(seq);
                return weappMsg;
            case revoke:
                RevokeMsg revokeMsg = objectMapper.readValue(chatData, RevokeMsg.class);
                revokeMsg.setContent(revokeMsg.getRevoke());
                revokeMsg.setSeq(seq);
                return revokeMsg;
            case card:
                CardMsg cardMsg = objectMapper.readValue(chatData, CardMsg.class);
                cardMsg.setContent(cardMsg.getCard());
                cardMsg.setSeq(seq);
                return cardMsg;
            case link:
                LinkMsg linkMsg = objectMapper.readValue(chatData, LinkMsg.class);
                linkMsg.setContent(linkMsg.getLink());
                linkMsg.setSeq(seq);
                return linkMsg;
            case news:
                NewsMsg newsMsg = objectMapper.readValue(chatData, NewsMsg.class);
                newsMsg.setContent(newsMsg.getNews());
                newsMsg.setSeq(seq);
                return newsMsg;
            case todo:
                TodoMsg todoMsg = objectMapper.readValue(chatData, TodoMsg.class);
                todoMsg.setContent(todoMsg.getTodo());
                todoMsg.setSeq(seq);
                return todoMsg;
            case agree:
                AgreeMsg agreeMsg = objectMapper.readValue(chatData, AgreeMsg.class);
                agreeMsg.setContent(agreeMsg.getAgree());
                agreeMsg.setSeq(seq);
                return agreeMsg;
            case mixed:
                //Todo 需要特殊处理
                MixedMsg mixedMsg = objectMapper.readValue(chatData, MixedMsg.class);
                mixedMsg.setContent(mixedMsg.getMixed());
                mixedMsg.setSeq(seq);
                return mixedMsg;
            case docmsg:
                DocMsg docMsg = objectMapper.readValue(chatData, DocMsg.class);
                docMsg.setContent(docMsg.getDoc());
                docMsg.setSeq(seq);
                return docMsg;
            case meeting:
                MeetingMsg meetingMsg = objectMapper.readValue(chatData, MeetingMsg.class);
                meetingMsg.setContent(meetingMsg.getMeeting());
                meetingMsg.setSeq(seq);
                return meetingMsg;
            case calendar:
                CalendarMsg calendarMsg = objectMapper.readValue(chatData, CalendarMsg.class);
                calendarMsg.setContent(calendarMsg.getCalendar());
                calendarMsg.setSeq(seq);
                return calendarMsg;
            case location:
                LocationMsg locationMsg = objectMapper.readValue(chatData, LocationMsg.class);
                locationMsg.setContent(locationMsg.getLocation());
                locationMsg.setSeq(seq);
                return locationMsg;
            case markdown:
                MarkdownMsg markdownMsg = objectMapper.readValue(chatData, MarkdownMsg.class);
                markdownMsg.setContent(markdownMsg.getMarkdown());
                markdownMsg.setSeq(seq);
                return markdownMsg;
            case redpacket:
                RedPacketMsg redPacketMsg = objectMapper.readValue(chatData, RedPacketMsg.class);
                redPacketMsg.setContent(redPacketMsg.getRedPacket());
                redPacketMsg.setSeq(seq);
                return redPacketMsg;
            case vote:
                break;
            case chatrecord:
                break;
            case collect:
                break;
            default:
                log.error("未知数据类型！");
        }

        return null;
    }


    /**
     * 上传至oss
     *
     * @param sdkField
     * @param mediaType
     * @param baseMsg
     */
    private void uploadOss(String sdkField, MediaType mediaType, BaseMsg baseMsg) {
        String suffix = mediaType.getSuffix();
        if (mediaType.equals(MediaType.file)) {
            FileMsg fileMsg = (FileMsg) baseMsg;
            suffix = "." + fileMsg.getFile().getFileext();
        }
        File file = null;
        try {
            file = SDKUtil.getMediaData(sdkField, suffix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Objects.isNull(file)) {
            return;
        }
        String path = ossService.upload(file, mediaType);
        baseMsg.setOssPath(path);
        file.delete();
    }
}
