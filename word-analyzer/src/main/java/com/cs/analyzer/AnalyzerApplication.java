package com.cs.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: CS
 * @Date: 2020/2/14 8:48 下午
 * @Description:
 */
@SpringBootApplication
@MapperScan(basePackages = "com.cs.analyzer.mapper")

public class AnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyzerApplication.class, args);
    }
}
