package com.cs.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: CS
 * @date: 2021/4/27 下午3:16
 * @description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = "com.cs.task.mapper")
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class);
    }
}
