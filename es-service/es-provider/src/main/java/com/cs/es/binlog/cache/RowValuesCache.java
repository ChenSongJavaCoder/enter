package com.cs.es.binlog.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keosn
 * @date 2019/4/3 17:32
 */
@Component
@Slf4j
public class RowValuesCache {

    /**
     * 内存缓存数据时间默认1分钟
     */
    private static final int MEMORY_CACHE_TIME = 60 * 1;

    @Autowired
    @Qualifier(value = "es_redistemplate")
    RedisTemplate<String, HashMap<String, Serializable>> redisTemplate;

    @Autowired
    MemoryCache memoryCache;

    public Boolean put(String key, HashMap<String, Serializable> value) {
        long start = System.currentTimeMillis();
        memoryCache.put(new MemoryCacheNode(key, value, MEMORY_CACHE_TIME));

        new RedisSavingThread(redisTemplate, key, value).start();

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

        result = redisTemplate.opsForValue().get(key);
        log.info("Getting from redis key:{} used: {}", key, System.currentTimeMillis() - start);

        memoryCache.put(new MemoryCacheNode(key, result, MEMORY_CACHE_TIME));
        return result;
    }

    private class RedisSavingThread extends Thread {
        private RedisTemplate<String, HashMap<String, Serializable>> redisTemplate;

        private String key;

        private HashMap<String, Serializable> value;

        public RedisSavingThread(RedisTemplate<String, HashMap<String, Serializable>> redisTemplate, String key, HashMap<String, Serializable> value) {
            this.redisTemplate = redisTemplate;
            this.key = key;
            this.value = value;
        }

        @Override
        public void run() {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    //TODO 内存缓存过期
}
