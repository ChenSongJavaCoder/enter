package com.cs.user.controller;

import com.cs.common.bean.MessageBuilder;
import com.cs.common.bean.PagedResult;
import com.cs.common.bean.Result;
import com.cs.user.api.UserApi;
import com.cs.user.converter.ListUserInfoConverter;
import com.cs.user.converter.SingleUserInfoConverter;
import com.cs.user.converter.config.UserConverterConfig;
import com.cs.user.entity.User;
import com.cs.user.mapper.UserCsMapper;
import com.cs.user.pojo.PagedModelRequest;
import com.cs.user.pojo.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.validation.Valid;
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

    @Autowired
    ListUserInfoConverter listUserInfoConverter;

    String defaultPassword = "123456";
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @Override
    public Result<String> createUser(UserInfo request) {
        User user = new User();

        if (checkExistByUserName(request.getUsername())) {
            return Result.failure()
                    .data("该用户名已被注册")
                    .build();
        }
        BeanUtils.copyProperties(request, user);
        // 初始密码  两种实现：1.初始固定密码：123456。登陆后修改 2.随机六位数密码，短信提醒
        // 具体结合业务场景选择实现方式
        user.setPassword(passwordEncoder.encode(defaultPassword));
        userCsMapper.insertSelective(user);
        return Result.success()
                .data(MessageBuilder.successMessage())
                .build();
    }

    @Override
    public Result<String> updateUser(UserInfo request) {
        User user = new User();
        BeanUtils.copyProperties(request, user, "password");
        userCsMapper.updateByPrimaryKeySelective(user);
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
        criteria.andEqualTo("username", username);
        User u = userCsMapper.selectOneByExample(example);

        return Result.success()
                .data(singleUserInfoConverter.convert(u, new UserConverterConfig().setShowPassword(true)))
                .build();
    }

    @Override
    public Result<PagedResult<UserInfo>> pagedUserInfoByModel(@Valid PagedModelRequest request, BindingResult bindingResult) {
        User u = new User();
        BeanUtils.copyProperties(request.getUserInfo(), u, "id");

        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        List<User> users = userCsMapper.select(u);
        PageInfo<User> pageInfo = new PageInfo<>(users);

        return Result.success()
                .data(new PagedResult<UserInfo>()
                        .setRows(listUserInfoConverter.convert(users))
                        .setTotal(pageInfo.getTotal()))
                .build();
    }


    private boolean checkExistByUserName(String name) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
//        example.orderBy("").desc().orderBy("").asc();
        List<User> users = userCsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return false;
        }
        return true;
    }
}
