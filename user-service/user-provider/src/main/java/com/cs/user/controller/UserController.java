package com.cs.user.controller;

import com.cs.common.bean.MessageBuilder;
import com.cs.common.bean.Result;
import com.cs.user.api.UserApi;
import com.cs.user.entity.User;
import com.cs.user.mapper.UserCsMapper;
import com.cs.user.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserController
 * @Author: CS
 * @Date: 2019/11/5 17:12
 * @Description: TODO 入参校验
 */
@Slf4j
@Validated
@RestController
public class UserController implements UserApi {

    @Autowired
    UserCsMapper userCsMapper;


    @Override
    public Result<String> createUser(UserInfo request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        userCsMapper.insertSelective(user);
        return Result.success()
                .data(MessageBuilder.successMessage())
                .build();
    }

    @Override
    public Result<String> updateUser(UserInfo request) {
        return null;
    }

    @Override
    public Result<UserInfo> getUserInfoById(Long userId) {
        return null;
    }

    @Override
    public Result<UserInfo> getUserInfoByUsername(String username) {
        return null;
    }
}
