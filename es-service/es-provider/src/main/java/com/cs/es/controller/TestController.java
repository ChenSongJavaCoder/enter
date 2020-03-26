package com.cs.es.controller;

import com.cs.es.common.EsIndices;
import com.cs.es.common.EsRestService;
import com.cs.es.model.DemoTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chenS
 * @Date 2019-12-04 14:57
 * @Description
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    EsRestService esRestService;

    ObjectMapper objectMapper = new ObjectMapper();


    @ApiOperation("测试连接")
    @GetMapping("client")
    public String testClient() throws JsonProcessingException {
        DemoTest demoTest = new DemoTest();
        demoTest.setName("cs");
        demoTest.setAge(22);
        demoTest.setSex(1);
        esRestService.createOrUpdateDocument(EsIndices.DEMO, null, objectMapper.writeValueAsString(demoTest));
        return esRestService.testClient();
    }


}
