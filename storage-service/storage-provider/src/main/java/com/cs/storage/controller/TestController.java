package com.cs.storage.controller;

import com.cs.storage.api.TestApi;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Author: CS
 * @Date: 2019/11/1 17:52
 * @Description:
 */
@RestController
public class TestController implements TestApi {

	@Override
	public String testApi() {
		return "storage service connect success!";
	}
}
