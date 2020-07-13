package com.cs.workwechat.service;

import com.cs.workwechat.config.WorkWechatConfig;
import com.cs.workwechat.constants.WorkWechatOpenApi;
import com.cs.workwechat.pojo.response.AbstractResponse;
import com.cs.workwechat.pojo.response.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: CS
 * @Date: 2020/6/2 4:09 下午
 * @Description:
 */
@Slf4j
@Service
public abstract class AbstractWorkWechatService {

    public static String token = null;
    /**
     * 过期时间秒数
     */
    public volatile long expireTime = 0;

//    @Autowired
//    DingTalkRobotClient dingTalkRobotClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WorkWechatConfig workWechatConfig;

    @Autowired
    private RestTemplate restTemplate;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public String getAccessToken() {
        //TODO: 优先查询缓存,REDIS
        //查询微信token
        String url = String.format(WorkWechatOpenApi.GET_TOKEN, workWechatConfig.getCorpId(), workWechatConfig.getSecret());
        TokenResponse tokenRes = getForResponse(url, TokenResponse.class);
        if (Objects.nonNull(tokenRes) && tokenRes.isSuccess()) {
            token = tokenRes.getAccess_token();
        } else {
            //TODO retry & remind
//            dingTalkRobotClient.sendTextMessage("企业微信获取token失败!");
            throw new IllegalStateException("企业微信获取token失败");
        }

        return token;
    }

    public <Response extends AbstractResponse> Response getForResponse(String url, Class<Response> clazz) {
        log.info("postForResponse 请求地址：{}", url);
        Response response = restTemplate.getForObject(url, clazz);
        log.info("postForResponse 返回结果：{}", response.toString());
        //TODO check response
        return response;
    }

    public <Request, Response extends AbstractResponse> Response postForResponse(String url, Request body, Class<Response> clazz) {
        log.info("postForResponse 请求地址：{} 参数：{}", url, body.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Request> entity = new HttpEntity<>(body, headers);
        Response response = restTemplate.postForObject(url, entity, clazz);
        log.info("postForResponse 返回结果：{}", response.toString());
        //TODO check response
        return response;
    }


}
