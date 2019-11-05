package com.cs.user.controller;

import com.cs.common.bean.MessageBuilder;
import com.cs.common.bean.Result;
import com.cs.user.api.UserApi;
import com.cs.user.converter.SingleUserInfoConverter;
import com.cs.user.entity.User;
import com.cs.user.mapper.UserCsMapper;
import com.cs.user.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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

    @Autowired
    SingleUserInfoConverter singleUserInfoConverter;


    @Override
    public Result<String> createUser(UserInfo request) {
        User user = new User();

        if (checkExistByUserName(request.getName())) {
            return Result.failure()
                    .data("该姓名已注册")
                    .build();
        }
        BeanUtils.copyProperties(request, user);
        userCsMapper.insertSelective(user);
        return Result.success()
                .data(MessageBuilder.successMessage())
                .build();
    }

    @Override
    public Result<String> updateUser(UserInfo request) {
        return Result.success()
                .data(MessageBuilder.successMessage())
                .build();
    }

    @Override
    public Result<Boolean> checkExistByUsername(String username) {
        return Result.success()
                .data(checkExistByUserName(username))
                .build();
    }

    @Override
    public Result<UserInfo> getUserInfoById(Long userId) {
        return Result.success()
                .data(singleUserInfoConverter.convert(userCsMapper.selectByPrimaryKey(userId)))
                .build();
    }

    @Override
    public Result<UserInfo> getUserInfoByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", username);
        User u = userCsMapper.selectOneByExample(example);

        return Result.success()
                .data(singleUserInfoConverter.convert(u))
                .build();
    }


    private boolean checkExistByUserName(String name) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        List<User> users = userCsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return false;
        }
        return true;
    }
}
