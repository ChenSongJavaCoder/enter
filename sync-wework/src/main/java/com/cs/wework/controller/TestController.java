package com.cs.wework.controller;

import com.cs.wework.entity.BaseMsg;
import com.cs.wework.mapper.WorkWechatMsgMapper;
import com.cs.wework.pojo.TextMsg;
import com.cs.wework.pojo.enums.MediaType;
import com.cs.wework.pojo.enums.MsgType;
import com.cs.wework.service.OssService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @Author: CS
 * @Date: 2020/3/19 3:03 下午
 * @Description:
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    WorkWechatMsgMapper workWechatMsgMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OssService ossService;

    //    @Scheduled(fixedRate = 600 * 1000)
    @GetMapping("/test")
    public String test() throws JsonProcessingException {

        BaseMsg o = workWechatMsgMapper.selectByPrimaryKey(2);
        log.info(objectMapper.writeValueAsString(o));
        try {
            if (o.getMsgtype().equals(MsgType.text)) {
                TextMsg.Text text = objectMapper.readValue((String) o.getContent(), TextMsg.Text.class);
                o.setContent(text);
            }
            String s = objectMapper.writeValueAsString(o);
            log.info(s);
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "错了呀";
    }

    @GetMapping("/upload")
    public void upload() {
        File file = new File("/Users/chensong/Desktop/图片/3cf309b12112e59b9886ebff5d4299bd.jp");
        ossService.upload(file, MediaType.image);
    }

}
