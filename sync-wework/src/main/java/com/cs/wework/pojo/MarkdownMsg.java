package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class MarkdownMsg extends BaseMsg<MarkdownMsg.Markdown> {

    private Markdown markdown;

    @Data
    public static class Markdown {

        private String content;
    }
}
