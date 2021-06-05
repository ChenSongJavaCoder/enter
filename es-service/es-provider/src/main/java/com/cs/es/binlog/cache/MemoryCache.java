package com.cs.es.binlog.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description: 内存缓存
 */
@Slf4j
@Component
public class MemoryCache implements Cache {

    private ConcurrentHashMap<String, CacheNode> cache = new ConcurrentHashMap<>();

    @Override
    public void put(CacheNode node) {
        cache.put(node.getKey(), node);
    }

    @Override
    public Map<String, Serializable> get(String key) {
        log.info("memory cache size: {}", cache.size());
        CacheNode node = cache.get(key);
        if (null == node) {
            return null;
        } else {
            return node.getValue();
        }
    }

    /**
     * 缓存失效
     */
    @PostConstruct
    public void expire() {
        new ExpireMonitor().start();
    }

    private class ExpireMonitor extends Thread {
        public ExpireMonitor() {
            super("MemoryCacheExpireMonitor");
        }

        @Override
        public void run() {
            do {
                cache.values().stream().filter(f -> f.getExpireTimeAt() < System.currentTimeMillis()).forEach(e -> cache.remove(e.getKey()));
            }
            while (true);
        }
    }

}
