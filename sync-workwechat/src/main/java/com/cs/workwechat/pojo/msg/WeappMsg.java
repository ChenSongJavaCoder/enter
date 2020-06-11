package com.cs.workwechat.pojo.msg;

import com.cs.workwechat.entity.BaseMsg;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: CS
 * @Date: 2020/3/19 11:12 上午
 * @Description:
 */
@Data
@Accessors(chain = true)
public class WeappMsg extends BaseMsg<WeappMsg.Weapp> {

    private Weapp weapp;

    @Data
    public static class Weapp {
        private String title;
        private String description;
        private String username;
        private String displayname;
    }

}
