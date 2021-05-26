package com.cs.es.binlog.cache;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/5/26 上午11:40
 * @description:
 */
public interface Cache {

    /**
     * 放入缓存
     *
     * @param cacheNode
     */
    void put(CacheNode cacheNode);

    /**
     * 获取缓存对象
     *
     * @param key
     * @return
     */
    Map<String, Serializable> get(String key);
}
