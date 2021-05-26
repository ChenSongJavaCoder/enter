package com.cs.es.binlog.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: CS
 * @date: 2021/5/26 上午11:46
 * @description:
 */
@Slf4j
@Component
public class RedisCache implements Cache {

    @Autowired
    @Qualifier(value = "es_redistemplate")
    RedisTemplate<String, Map<String, Serializable>> redisTemplate;

    @Override
    public void put(CacheNode cacheNode) {
        Assert.notNull(cacheNode, "memoryCacheNode can't be null");
        long start = System.currentTimeMillis();
        try {
            redisTemplate.opsForValue().set(cacheNode.getKey(), cacheNode.getValue(), cacheNode.getExpireTime(), TimeUnit.MILLISECONDS);
            log.info("saving redis cache used: {}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.warn("saving redis cache error! key:{} value:{} exception:{}", cacheNode.getKey(), cacheNode.getValue(), e.getMessage());
        }
    }

    @Override
    public Map<String, Serializable> get(String key) {
        Assert.notNull(key, "key can't be null");
        long start = System.currentTimeMillis();
        try {
            Map<String, Serializable> value = redisTemplate.opsForValue().get(key);
            log.info("get redis cache used: {} value: {}", (System.currentTimeMillis() - start), value);
            return value;
        } catch (Exception e) {
            log.warn("get redis cache error! key:{}", key);
        }
        return null;
    }
}
