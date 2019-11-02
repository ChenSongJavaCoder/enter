package com.cs.user.controller;

import com.cs.user.api.TestApi;
import com.cs.user.entity.UserCs;
import com.cs.user.mapper.UserCsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName: TestController
 * @Author: CS
 * @Date: 2019/11/2 11:03
 * @Description:
 */
@RestController
public class TestController implements TestApi {

	@Autowired
	UserCsMapper userCsMapper;

	String defaultPassword = "123456";

	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Override
	public String test() {
		int record = userCsMapper.insertSelective(new UserCs().setAge(26).setName("cs_" + LocalDateTime.now().toString()).setPassword(passwordEncoder.encode(defaultPassword)));
		if (1 == record) {
			return "insert user success!";
		}
		return "insert user fail!";
	}

	@Override
	public String testLogin() {
		UserCs userCs = new UserCs();
		userCs.setId(3L);
		UserCs a = userCsMapper.selectOne(userCs);
		if (Objects.nonNull(a) && passwordEncoder.matches(defaultPassword, a.getPassword())) {
			return "账号密码匹配成功！";
		}
		return "登录失败！";
	}
}
