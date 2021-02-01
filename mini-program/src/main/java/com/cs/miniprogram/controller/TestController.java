package com.cs.miniprogram.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Author: CS
 * @Date: 2019/11/1 17:52
 * @Description:
 */
@RestController
public class TestController {

    @GetMapping("testApi")
    @ApiOperation(value = "test")
    public String testApi() {
        return "mini program service connect success!";
    }
}
