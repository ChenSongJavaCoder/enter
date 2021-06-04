package com.cs.es.binlog.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description: binlog数据缓存 memory + redis
 */
@Component
@Slf4j
public class RowValuesCache {

    /**
     * 内存缓存数据时间默认1分钟,毫秒单位
     */
    private static final int MEMORY_CACHE_TIME = 1000 * 60 * 3;
    /**
     * redis缓存时间1天
     */
    private static final int REDIS_CACHE_TIME = MEMORY_CACHE_TIME * 60 * 24;


    @Autowired
    MemoryCache memoryCache;

    @Autowired
    RedisCache redisCache;

    /**
     * 系统缓存
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean put(String key, Map<String, Serializable> value) {
        long start = System.currentTimeMillis();
        // 系统缓存
        memoryCache.put(new CacheNode(key, value, MEMORY_CACHE_TIME));
        // redis缓存
        redisCache.put(new CacheNode(key, value, REDIS_CACHE_TIME));
        log.info("Saving key:{} used: {}", key, System.currentTimeMillis() - start);
        return Boolean.TRUE;
    }

    public Map<String, Serializable> get(String key) {
        long start = System.currentTimeMillis();
        Map<String, Serializable> result = memoryCache.get(key);
        if (null != result) {
            log.info("Getting from memory key:{} used: {} ", key, System.currentTimeMillis() - start);
            return result;
        }

        result = redisCache.get(key);
        log.info("Getting from redis key:{} used: {}", key, System.currentTimeMillis() - start);

        memoryCache.put(new CacheNode(key, result, MEMORY_CACHE_TIME));
        return result;
    }

}
