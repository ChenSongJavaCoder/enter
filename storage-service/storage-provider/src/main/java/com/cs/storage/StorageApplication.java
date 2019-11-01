package com.cs.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: StorageApplication
 * @Author: CS
 * @Date: 2019/11/1 16:38
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageApplication.class, args);
	}
}
