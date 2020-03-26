package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class NewsMsg extends BaseMsg<NewsMsg.News> {

    private News news;

    @Data
    public static class News {

        /**
         * 图文消息标题。String类型
         */
        private String title;

        /**
         * 图文消息描述。String类型
         */
        private String description;

        /**
         * 图文消息点击跳转地址。String类型
         */
        private String url;

        /**
         * 图文消息配图的url。String类型
         */
        private String picurl;
    }
}
