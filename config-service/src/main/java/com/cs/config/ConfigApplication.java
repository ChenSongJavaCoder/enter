package com.cs.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName: ConfigApplication
 * @Author: CS
 * @Date: 2019/10/31 13:56
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigApplication {
}
