package com.cs.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName: UserApplication
 * @Author: CS
 * @Date: 2019/11/2 9:47
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.cs.user.mapper")
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
}
