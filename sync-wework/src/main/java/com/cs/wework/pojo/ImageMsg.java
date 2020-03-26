package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/19 11:12 上午
 * @Description:
 */
@Data
@Accessors(chain = true)
public class ImageMsg extends BaseMsg<ImageMsg.Image> {

    private Image image;

    @Data
    public static class Image {
        private String md5sum;
        private String sdkfileid;
        private Integer filesize;

    }

}
