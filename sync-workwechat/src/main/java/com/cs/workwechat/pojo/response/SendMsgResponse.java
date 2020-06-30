package com.cs.workwechat.pojo.response;

import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/6/18 2:47 下午
 * @Description: 发送消息返回结果
 * 如果部分接收人无权限或不存在，发送仍然执行，但会返回无效的部分（即invaliduser或invalidparty或invalidtag），常见的原因是接收人不在应用的可见范围内。
 * 如果全部接收人无权限或不存在，则本次调用返回失败，errcode为81013。
 * 返回包中的userid，不区分大小写，统一转为小写
 */
@Data
public class SendMsgResponse extends AbstractResponse {

    private String invaliduser;

    private String invalidparty;

    private String invalidtag;
}
