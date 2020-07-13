package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;

import java.util.List;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class MeetingVoiceCallMsg extends BaseMsg<MeetingVoiceCallMsg.MeetingVoiceCall> {

    private String voiceid;

    private MeetingVoiceCall meeting_voice_call;

    @Data
    public static class MeetingVoiceCall {


        /**
         * endtime : 1594197635
         * sdkfileid : CpsBKjAqd0xhb2JWRUJldGtwcE5DVTB6UjRUalN6c09vTjVyRnF4YVJ5M24rZC9YcHF3cHRPVzRwUUlaMy9iTytFcnc0SlBkZDU1YjRNb0MzbTZtRnViOXV5WjUwZUIwKzhjbU9uRUlxZ3pyK2VXSVhUWVN2ejAyWFJaTldGSkRJVFl0aUhkcVdjbDJ1L2RPbjJsRlBOamJaVDNnPT0SOE5EZGZNVFk0T0RnMU16YzVNVGt5T1RJMk9GOHhNalk0TXpBeE9EZzJYekUxT1RReE9UYzJNemM9GiA3YTYyNzA3NTY4Nzc2MTY3NzQ2MTY0NzA2ZTc4NjQ2OQ==
         * demofiledata : [{"filename":"65eb1cdd3e7a3c1740ecd74220b6c627.docx","demooperator":"wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA","starttime":1594197599,"endtime":1594197609}]
         * sharescreendata : [{"share":"wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA","starttime":1594197624,"endtime":1594197624}]
         */
        private int endtime;
        private String sdkfileid;
        private List<DemofiledataBean> demofiledata;
        private List<SharescreendataBean> sharescreendata;


        @Data
        public static class DemofiledataBean {
            /**
             * filename : 65eb1cdd3e7a3c1740ecd74220b6c627.docx
             * demooperator : wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA
             * starttime : 1594197599
             * endtime : 1594197609
             */
            private String filename;
            private String demooperator;
            private int starttime;
            private int endtime;
        }

        @Data
        public static class SharescreendataBean {
            /**
             * share : wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA
             * starttime : 1594197624
             * endtime : 1594197624
             */
            private String share;
            private int starttime;
            private int endtime;
        }
    }
}
