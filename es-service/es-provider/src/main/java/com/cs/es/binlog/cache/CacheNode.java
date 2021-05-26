package com.cs.es.binlog.cache;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/5/8 下午2:22
 * @description: 缓存对象
 */
@Data
public class CacheNode {

    private String key;

    private Map<String, Serializable> value;

    private long expireTime;

    private long expireTimeAt;


    public CacheNode(String key, Map<String, Serializable> value, long expireTime) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
        this.expireTimeAt = System.currentTimeMillis() + expireTime;
    }
}
