package com.cs.es.binlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author: CS
 * @date: 2021/5/7 下午5:52
 * @description:
 */
@Slf4j
@Component
public class BinlogWatcher {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HandlerDispatcher handlerDispatcher;


    @PostConstruct
    public void onEvent() {
        new SynchronizeThread().start();
    }


    private class SynchronizeThread extends Thread {
        @Override
        public void run() {
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
            try {
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
