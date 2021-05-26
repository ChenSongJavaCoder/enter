package com.cs.es.binlog.config;

import com.cs.es.binlog.HandlerDispatcher;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.github.shyiko.mysql.binlog.jmx.BinaryLogClientStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author: CS
 * @date: 2021/5/26 下午5:29
 * @description:
 */
@Slf4j
@Configuration
public class BinaryLogClientConfig {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    HandlerDispatcher handlerDispatcher;

    @Bean
    @DependsOn(value = "handlerDispatcher")
    public BinaryLogClient binaryLogClient() {
        String url = dataSourceProperties.getUrl();
        url = url.substring(url.indexOf("//") + 2);
        String host = url.substring(0, url.indexOf(":"));
        String port = url.substring(url.indexOf(":") + 1, url.indexOf("/"));
        BinaryLogClient client = new BinaryLogClient(host, Integer.valueOf(port), "test", "test");
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
//                    EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
//                    EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY,
                EventDeserializer.CompatibilityMode.INVALID_DATE_AND_TIME_AS_ZERO
        );
        client.setEventDeserializer(eventDeserializer);
        client.setServerId(1);
        client.registerEventListener((event ->
                handlerDispatcher.getHandler(event).ifPresent(handler -> handler.handle(event))
        ));
        return client;
    }

    @Bean
    public BinaryLogClientStatistics binaryLogClientStatistics(@Autowired BinaryLogClient binaryLogClient) {
        return new BinaryLogClientStatistics(binaryLogClient);
    }
}
