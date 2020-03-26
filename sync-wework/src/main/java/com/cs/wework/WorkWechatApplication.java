package com.cs.wework;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: CS
 * @Date: 2020/3/12 5:13 下午
 * @Description: 彪悍的人生不需要解释
 */
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.cs.workwechat.mapper")
public class WorkWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkWechatApplication.class, args);
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
