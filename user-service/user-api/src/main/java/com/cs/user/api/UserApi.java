package com.cs.user.api;

import com.cs.common.bean.PagedResult;
import com.cs.common.bean.Result;
import com.cs.common.swagger.ApiTags;
import com.cs.user.api.get.GetUserApi;
import com.cs.user.pojo.PagedModelRequest;
import com.cs.user.pojo.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @ClassName: UserApi
 * @Author: CS
 * @Date: 2019/11/5 14:50
 * @Description:
 */
@Api(tags = ApiTags.USER_OPERATION)
public interface UserApi extends GetUserApi {

    /**
     * 新增用户
     *
     * @param request
     * @return
     */
    @ApiOperation("新增用户")
    @PostMapping(value = "createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<String> createUser(@RequestBody UserInfo request);

    /**
     * 修改用户
     *
     * @param request
     * @return
     */
    @ApiOperation("修改用户")
    @PostMapping(value = "updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<String> updateUser(@RequestBody UserInfo request);


    /**
     * 根据userInfo查询用户信息
     *
     * @param request
     * @return
     */
    @ApiOperation("根据username查询用户信息")
    @PostMapping(value = "pagedUserInfoByModel", produces = MediaType.APPLICATION_JSON_VALUE)
    Result<PagedResult<UserInfo>> pagedUserInfoByModel(@RequestBody @Valid PagedModelRequest request, BindingResult bindingResult);

}
