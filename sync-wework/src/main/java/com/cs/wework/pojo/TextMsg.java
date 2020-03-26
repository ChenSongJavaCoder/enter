package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/18 3:27 下午
 * @Description:
 */
@Data
@Accessors(chain = true)
public class TextMsg extends BaseMsg<TextMsg.Text> {

    private Text text;

    @Data
    public static class Text {
        private String content;
    }
}


