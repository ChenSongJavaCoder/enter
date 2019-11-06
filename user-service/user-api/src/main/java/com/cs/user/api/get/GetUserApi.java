package com.cs.user.api.get;

import com.cs.common.bean.Result;
import com.cs.user.pojo.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author chenS
 * @Date 2019-11-06 19:51
 * @Description
 **/
public interface GetUserApi {

    /**
     * 根据用户名验证是否已存在
     *
     * @param username
     * @return
     */
    @ApiOperation("根据用户名验证是否已存在")
    @GetMapping(value = "checkExistByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<Boolean> checkExistByUsername(@PathVariable(value = "username") String username);


    /**
     * 根据ID查询用户信息
     *
     * @param userId
     * @return
     */
    @ApiOperation("根据ID查询用户信息")
    @GetMapping(value = "getUserInfoById/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<UserInfo> getUserInfoById(@PathVariable(value = "userId") Long userId);

    /**
     * 根据username查询用户信息
     *
     * @param username
     * @return
     */
    @ApiOperation("根据username查询用户信息")
    @GetMapping(value = "getUserInfoByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<UserInfo> getUserInfoByUsername(@PathVariable(value = "username") String username);

}
