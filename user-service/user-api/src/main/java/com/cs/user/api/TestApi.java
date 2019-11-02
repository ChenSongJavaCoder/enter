package com.cs.user.api;

import com.cs.user.pojo.UserDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName: TestApi
 * @Author: CS
 * @Date: 2019/11/2 11:05
 * @Description:
 */
public interface TestApi {

	@ApiOperation("test")
	@GetMapping("test")
	String test();

	@ApiOperation("testLogin")
	@GetMapping("testLogin")
	String testLogin();

	@ApiOperation("loadByName")
	@GetMapping(value = "loadByName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	UserDto loadByName(@PathVariable(value = "name") String name);
}
