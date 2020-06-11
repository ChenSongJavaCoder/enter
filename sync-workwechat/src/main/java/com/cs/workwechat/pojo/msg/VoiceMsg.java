package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/18 3:27 下午
 * @Description: 语音消息
 */
@Data
@Accessors(chain = true)
public class VoiceMsg extends BaseMsg<VoiceMsg.Voice> {

    private Voice voice;

    @Data
    public static class Voice {

        private int voice_size;

        private int play_length;

        private String sdkfileid;

        private String md5sum;
    }
}


