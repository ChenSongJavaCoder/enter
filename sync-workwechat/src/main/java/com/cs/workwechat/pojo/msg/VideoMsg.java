package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/18 3:27 下午
 * @Description: 视频消息
 */
@Data
@Accessors(chain = true)
public class VideoMsg extends BaseMsg<VideoMsg.Video> {

    private Video video;

    @Data
    public static class Video {

        private int filesize;

        private int play_length;

        private String sdkfileid;

        private String md5sum;
    }
}


