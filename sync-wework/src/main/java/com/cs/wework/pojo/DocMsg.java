package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class DocMsg extends BaseMsg<DocMsg.Doc> {

    private Doc doc;

    @Data
    public static class Doc {

        /**
         * 在线文档名称
         */
        private String title;

        /**
         * 在线文档链接
         */
        private String link_url;

        /**
         * 在线文档创建者。本企业成员创建为userid；外部企业成员创建为external_userid
         */
        private String doc_creator;
    }
}
