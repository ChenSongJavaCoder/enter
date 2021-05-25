package com.cs.es.binlog.cache;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keosn
 * @date 2019/4/13 11:25
 */
@Component
public class MemoryCache {

    private ConcurrentHashMap<String, MemoryCacheNode> cache = new ConcurrentHashMap<>();

    public void put(MemoryCacheNode node) {
        cache.put(node.getKey(), node);
    }

    public Map<String, Serializable> get(String key) {
        MemoryCacheNode node = cache.get(key);
        if (null == node) {
            return null;
        } else {
            return node.getValue();
        }
    }

}
