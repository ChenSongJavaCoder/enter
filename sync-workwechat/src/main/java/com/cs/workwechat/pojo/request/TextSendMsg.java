package com.cs.workwechat.pojo.request;

import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/6/18 2:24 下午
 * @Description: 发送文本消息
 */
@Data
public class TextSendMsg extends BaseSendMsg {

    private String msgtype = "text";

    private Text text;

    public static void main(String[] args) {
        BaseSendMsg msg = new TextSendMsg();
        msg.init();
    }

    @Data
    public static class Text {
        private String content;
    }
}
