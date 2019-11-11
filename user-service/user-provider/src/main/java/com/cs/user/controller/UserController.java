package com.cs.user.controller;

import com.cs.common.bean.MessageBuilder;
import com.cs.common.bean.PagedResult;
import com.cs.common.bean.Result;
import com.cs.common.util.UUIDUtil;
import com.cs.message.pojo.event.EventInfo;
import com.cs.message.pojo.event.EventType;
import com.cs.user.api.UserApi;
import com.cs.user.converter.ListUserInfoConverter;
import com.cs.user.converter.SingleUserInfoConverter;
import com.cs.user.converter.config.UserConverterConfig;
import com.cs.user.entity.User;
import com.cs.user.feign.FeignRabbitMqApi;
import com.cs.user.mapper.UserMapper;
import com.cs.user.pojo.PagedModelRequest;
import com.cs.user.pojo.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDateTime;
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
    UserMapper userMapper;

    @Autowired
    SingleUserInfoConverter singleUserInfoConverter;

    @Autowired
    ListUserInfoConverter listUserInfoConverter;

    @Autowired
    FeignRabbitMqApi feignRabbitMqApi;

    ObjectMapper objectMapper = new ObjectMapper();

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
        userMapper.insertSelective(user);
//        try {
//            String userStr = objectMapper.writeValueAsString(user);
//            EventInfo<User> eventInfo = new EventInfo()
//                    .setEventId(UUIDUtil.uuid32())
//                    .setEventType(EventType.CREATE_USER)
//                    .setEventParam(userStr)
//                    .setEventTime(LocalDateTime.now())
//                    ;
//            feignRabbitMqApi.publish(eventInfo);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        request.setId(user.getId());
        EventInfo<UserInfo> eventInfo = new EventInfo<UserInfo>()
                .setEventId(UUIDUtil.uuid32())
                .setEventType(EventType.CREATE_USER)
                .setEventParam(request)
                .setEventTime(LocalDateTime.now());
        feignRabbitMqApi.publish(eventInfo);
        return Result.success()
                .data(MessageBuilder.successMessage())
                .build();
    }

    @Override
    public Result<String> updateUser(UserInfo request) {
        User user = new User();
        BeanUtils.copyProperties(request, user, "password");
        userMapper.updateByPrimaryKeySelective(user);
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
                .data(singleUserInfoConverter.convert(userMapper.selectByPrimaryKey(userId)))
                .build();
    }

    @Override
    public Result<UserInfo> getUserInfoByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        User u = userMapper.selectOneByExample(example);

        return Result.success()
                .data(singleUserInfoConverter.convert(u, new UserConverterConfig().setShowPassword(true)))
                .build();
    }

    @Override
    public Result<PagedResult<UserInfo>> pagedUserInfoByModel(@Valid PagedModelRequest request, BindingResult bindingResult) {
        User u = new User();
        BeanUtils.copyProperties(request.getUserInfo(), u, "id");

        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        List<User> users = userMapper.select(u);
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
	    criteria.andEqualTo("username", name);
//        example.orderBy("").desc().orderBy("").asc();
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return false;
        }
        return true;
    }
}
