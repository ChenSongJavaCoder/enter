package com.cs.wework.pojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: CS
 * @Date: 2020/3/12 5:13 下午
 * @Description: 媒体文件类型
 */
@Getter
@AllArgsConstructor
public enum MediaType {

    image("图片", ".jpg", "image/jpeg"),
    emotion("表情", ".jpeg", "image/jpeg"),
    voice("声音", ".amr", "audio/amr"),
    video("视频", ".mp4", "video/mpeg4"),
    file("文件", null, null),
    ;

    private String desc;
    private String suffix;
    private String contentType;

}
