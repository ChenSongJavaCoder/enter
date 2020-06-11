package com.cs.workwechat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:02 下午
 * @Description:
 */
@Getter
@Component
public class WorkWechatConfig {

    @Value("${workWechat.config.corpid}")
    private String corpId;

    @Value("${workWechat.config.agentid}")
    private String agentId;

    @Value("${workWechat.config.secret}")
    private String secret;

    @Value("${workWechat.config.user.token}")
    private String userToken;

    @Value("${workWechat.config.user.encodingAESKey}")
    private String userEncodingAESKey;

    @Value("${workWechat.config.external.token}")
    private String externalToken;

    @Value("${workWechat.config.external.encodingAESKey}")
    private String externalEncodingAESKey;
}
