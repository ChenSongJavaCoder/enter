package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/21 10:16 上午
 * @Description:
 */
@Data
@Accessors(chain = true)
public class EmotionMsg extends BaseMsg<EmotionMsg.Emotion> {

    private Emotion emotion;

    @Data
    public static class Emotion {

        private String sdkfileid;
        private String md5sum;
        /**
         * 表情类型，png或者gif.1表示gif 2表示png。Uint32类型
         */
        private Integer type;
        /**
         * 表情图片宽度。Uint32类型
         */
        private Integer width;
        /**
         * 表情图片高度。Uint32类型
         */
        private Integer height;
        /**
         * 资源的文件大小。Uint32类型
         */
        private Integer imagesize;
    }
}
