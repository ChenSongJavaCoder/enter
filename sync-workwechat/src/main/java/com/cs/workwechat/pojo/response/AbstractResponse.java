package com.cs.workwechat.pojo.response;

import com.cs.workwechat.constants.WorkWechatOpenApi;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:35 下午
 * @Description:
 */
@Data
public abstract class AbstractResponse {

    private int errcode;
    private String errmsg;

    /**
     * 接口是否请求成功
     *
     * @return
     */
    public Boolean isSuccess() {
        return WorkWechatOpenApi.SUCCESS_CODE == errcode;
    }

}
