package com.cs.miniprogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: CS
 * @date: 2020/11/24 10:15 上午
 * @description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MiniProgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniProgramApplication.class);
    }
}
