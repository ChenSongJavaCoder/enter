package com.cs.user.controller;

import com.cs.user.api.TestApi;
import com.cs.user.entity.User;
import com.cs.user.mapper.UserCsMapper;
import com.cs.user.pojo.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName: TestController
 * @Author: CS
 * @Date: 2019/11/2 11:03
 * @Description:
 */
@Slf4j
@Validated
@RestController
public class TestController implements TestApi {

	@Autowired
	UserCsMapper userCsMapper;

	String defaultPassword = "123456";

	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Override
	public String test() {
		int record = userCsMapper.insertSelective(new User().setAge(26).setUsername("cs_" + LocalDateTime.now().toString()).setPassword(passwordEncoder.encode(defaultPassword)));
		if (1 == record) {
			return "insert user success!";
		}
		return "insert user fail!";
	}

	@Override
	public String testLogin() {
        User user = new User();
        user.setId(3L);
        User a = userCsMapper.selectOne(user);
		if (Objects.nonNull(a) && passwordEncoder.matches(defaultPassword, a.getPassword())) {
			return "账号密码匹配成功！";
		}
		return "登录失败！";
	}

	@Override
	public UserDto getUserByUsername(String name) {
        User user = null;
		try {
			user = userCsMapper.selectOne(new User().setUsername(name));
		} catch (Exception e) {
			log.error(e.getCause().getMessage());
		}
		UserDto userDto = new UserDto();
        if (Objects.nonNull(user)) {
            BeanUtils.copyProperties(user, userDto);
		}
		return userDto;
	}
}
