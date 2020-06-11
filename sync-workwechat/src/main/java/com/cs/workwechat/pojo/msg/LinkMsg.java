package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/21 10:16 上午
 * @Description:
 */
@Data
@Accessors(chain = true)
public class LinkMsg extends BaseMsg<LinkMsg.Link> {

    private Link link;

    @Data
    public static class Link {
        private String title;
        private String description;
        private String link_url;
        private String image_url;
    }
}
