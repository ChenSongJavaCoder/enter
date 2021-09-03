package com.cs.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * @author: CS
 * @date: 2021/8/12 下午4:50
 * @description:
 */
@EnableZipkinServer
@SpringBootApplication
public class ZipkinTraceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinTraceApplication.class, args);
    }
}
