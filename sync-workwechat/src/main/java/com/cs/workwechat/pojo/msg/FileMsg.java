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
public class FileMsg extends BaseMsg<FileMsg.File> {

    private File file;

    @Data
    public static class File {

        private String sdkfileid;
        private String md5sum;
        /**
         * 文件名称。String类型
         */
        private String filename;
        /**
         * 文件类型后缀。String类型
         */
        private String fileext;
        /**
         * 资源的文件大小。Uint32类型
         */
        private Integer filesize;
    }
}
