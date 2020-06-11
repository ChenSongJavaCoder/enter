package com.cs.workwechat.pojo.response;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:21 下午
 * @Description:
 */
@Data
@ToString
public class TokenResponse extends AbstractResponse {

    /**
     * errcode : 0
     * errmsg : ok
     * access_token : accesstoken000001
     * expires_in : 7200
     */
    private String access_token;
    private int expires_in;

}
