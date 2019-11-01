package com.cs.biz.controller;

import com.cs.biz.feign.storage.FeignStorageApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Author: CS
 * @Date: 2019/11/1 15:40
 * @Description: 测试类
 */
@RestController
@RequestMapping(value = "test")
public class TestController {

	@Autowired
	FeignStorageApi feignStorageApi;

	@ApiOperation(value = "连接测试")
	@GetMapping("client")
	public String client() {
		return "biz-service connect success!";
	}


	@ApiOperation(value = "feignClient连接测试")
	@GetMapping("feignClient")
	public String feignClient() {
		return feignStorageApi.testApi();
	}

}
