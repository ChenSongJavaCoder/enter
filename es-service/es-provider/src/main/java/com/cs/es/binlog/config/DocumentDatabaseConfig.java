package com.cs.es.binlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: CS
 * @date: 2021/5/26 下午8:01
 * @description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch.document")
public class DocumentDatabaseConfig {
    private String path;
    private Long serviceId;
    private String dbHost;
    private Integer dbPort;
    private String username;
    private String password;
}
