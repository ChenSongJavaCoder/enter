package com.cs.message;

import com.cs.message.client.UserStreamClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName: UserApplication
 * @Author: CS
 * @Date: 2019/11/2 9:47
 * @Description: 统一管理事件消息
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.cs.message.mapper")
@EnableBinding(value = {UserStreamClient.class})
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
