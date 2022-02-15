package com.cs.es.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.jmx.BinaryLogClientStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    BinaryLogClient client;

    @Autowired
    BinaryLogClientStatistics binaryLogClientStatistics;

    @PostConstruct
    public void start() {
        new SynchronizeThread(client).start();
        new BinaryLogClientStatisticsThread(binaryLogClientStatistics).start();
    }

    /**
     * 同步连接线程
     */
    private class SynchronizeThread extends Thread {
        private BinaryLogClient client;

        public SynchronizeThread(BinaryLogClient client) {
            super("BinlogSynchronize");
            this.client = client;
        }

        @Override
        public void run() {
            try {
                log.info("BinaryLogClient binlogFile:{} binlogPosition:{}", client.getBinlogFilename(), client.getBinlogPosition());
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * binlog统计线程，
     * todo 使用计划线程池
     */
    private class BinaryLogClientStatisticsThread extends Thread {
        private BinaryLogClientStatistics binaryLogClientStatistics;

        public BinaryLogClientStatisticsThread(BinaryLogClientStatistics binaryLogClientStatistics) {
            super("BinaryLogClientStatistics");
            this.binaryLogClientStatistics = binaryLogClientStatistics;
        }

        @Override
        public void run() {
            do {
                log.info("binaryLogClientStatistics: {}", binaryLogClientStatistics.getTotalBytesReceived());
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }


}
