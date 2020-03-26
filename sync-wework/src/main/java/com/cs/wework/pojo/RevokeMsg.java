package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/18 3:27 下午
 * @Description: 撤回消息
 */
@Data
@Accessors(chain = true)
public class RevokeMsg extends BaseMsg<RevokeMsg.Revoke> {

    private Revoke revoke;

    @Data
    public static class Revoke {
        /**
         * 标识撤回的原消息的msgid。String类型
         */
        private String pre_msgid;
    }
}


