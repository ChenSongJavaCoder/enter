package com.cs.es;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @Author chenS
 * @Date 2019-12-04 14:40
 * @Description
 **/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.cs.es.mapper")
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

}
