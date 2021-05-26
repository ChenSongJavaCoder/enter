package com.cs.es.binlog.config;

import com.cs.es.binlog.HandlerDispatcher;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.github.shyiko.mysql.binlog.jmx.BinaryLogClientStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * @author: CS
 * @date: 2021/5/26 下午5:29
 * @description:
 */
@Slf4j
@Configuration
public class BinaryLogClientConfig {

    public static final String BINLOG_FILE_PREFIX = "BinlogFile-";

    public static final String BINLOG_POSITION_PREFIX = "BinlogPosition-";

    @Autowired
    DocumentDatabaseConfig documentDatabaseConfig;

    @Autowired
    HandlerDispatcher handlerDispatcher;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @Bean
    @DependsOn(value = {"handlerDispatcher"})
    public BinaryLogClient binaryLogClient() {
        // binlog file 避免多个订阅者影响了数据同步
        String binFile = redisTemplate.opsForValue().get(BINLOG_FILE_PREFIX + documentDatabaseConfig.getServiceId());
        String binPosition = redisTemplate.opsForValue().get(BINLOG_POSITION_PREFIX + documentDatabaseConfig.getServiceId());
        BinaryLogClient client = new BinaryLogClient(documentDatabaseConfig.getDbHost(), documentDatabaseConfig.getDbPort(), documentDatabaseConfig.getUsername(), documentDatabaseConfig.getPassword());
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
//                    EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
//                    EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY,
                EventDeserializer.CompatibilityMode.INVALID_DATE_AND_TIME_AS_ZERO
        );
        if (StringUtils.hasText(binFile) && StringUtils.hasText(binPosition)) {
            client.setBinlogFilename(binFile);
            client.setBinlogPosition(Long.valueOf(binPosition));
        }
        client.setEventDeserializer(eventDeserializer);
        client.setServerId(documentDatabaseConfig.getServiceId());
        client.registerEventListener((event ->
                handlerDispatcher.getHandler(event).ifPresent(handler -> handler.handle(event))
        ));
        redisTemplate.opsForValue().set(BINLOG_FILE_PREFIX + documentDatabaseConfig.getServiceId(), client.getBinlogFilename());
        redisTemplate.opsForValue().set(BINLOG_POSITION_PREFIX + documentDatabaseConfig.getServiceId(), String.valueOf(client.getBinlogPosition()));

        return client;
    }

    @Bean
    public BinaryLogClientStatistics binaryLogClientStatistics(@Autowired BinaryLogClient binaryLogClient) {
        return new BinaryLogClientStatistics(binaryLogClient);
    }
}
