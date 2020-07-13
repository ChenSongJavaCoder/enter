package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class VoipDocShareMsg extends BaseMsg<VoipDocShareMsg.VoipDocShare> {

    private String voipid;

    private VoipDocShare voip_doc_share;

    @Data
    public static class VoipDocShare {


        /**
         * filename : 欢迎使用微盘.pdf.pdf
         * md5sum : ff893900f24e55e216e617a40e5c4648
         * filesize : 4400654
         * sdkfileid : CpsBKjAqZUlLdWJMd2gvQ1JxMzd0ZjlpdW5mZzJOOE9JZm5kbndvRmRqdnBETjY0QlcvdGtHSFFTYm95dHM2VlllQXhkUUN5KzRmSy9KT3pudnA2aHhYZFlPemc2aVZ6YktzaVh3YkFPZHlqNnl2L2MvcGlqcVRjRTlhZEZsOGlGdHJpQ2RWSVNVUngrVFpuUmo3TGlPQ1BJemlRPT0SOE5EZGZNVFk0T0RnMU16YzVNVGt5T1RJMk9GODFNelUyTlRBd01qQmZNVFU1TkRFNU9USTFOZz09GiA3YTcwNmQ2Zjc5NjY3MDZjNjY2Zjc4NzI3NTZmN2E2YQ==
         */
        private String filename;
        private String md5sum;
        private int filesize;
        private String sdkfileid;

    }
}
