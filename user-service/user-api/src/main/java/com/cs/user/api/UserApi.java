package com.cs.user.api;

import com.cs.common.bean.Result;
import com.cs.common.swagger.ApiTags;
import com.cs.user.pojo.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName: UserApi
 * @Author: CS
 * @Date: 2019/11/5 14:50
 * @Description:
 */
@Api(tags = ApiTags.USER_OPERATION)
public interface UserApi {

	/**
	 * 新增用户
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation("新增用户")
	@PostMapping(value = "createUser", produces = MediaType.APPLICATION_JSON_VALUE)
	Result<String> createUser(UserInfo request);

	/**
	 * 修改用户
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation("修改用户")
	@PostMapping(value = "updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
	Result<String> updateUser(UserInfo request);


	/**
	 * 根据ID查询用户信息
	 *
	 * @param userId
	 * @return
	 */
	@ApiOperation("根据ID查询用户信息")
	@GetMapping(value = "getUserInfoById/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	Result<UserInfo> getUserInfoById(@PathVariable Long userId);

	/**
	 * 根据username查询用户信息
	 *
	 * @param username
	 * @return
	 */
	@ApiOperation("根据username查询用户信息")
	@GetMapping(value = "getUserInfoByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	Result<UserInfo> getUserInfoByUsername(@PathVariable String username);

}
