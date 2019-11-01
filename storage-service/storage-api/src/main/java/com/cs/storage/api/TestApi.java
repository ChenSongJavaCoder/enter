package com.cs.storage.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName: TestApi
 * @Author: CS
 * @Date: 2019/11/1 17:36
 * @Description:
 */
public interface TestApi {

	@ApiOperation("测试接口")
	@GetMapping(value = "/testApi")
	String testApi();
}
