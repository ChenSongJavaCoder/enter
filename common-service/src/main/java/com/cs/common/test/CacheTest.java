package com.cs.common.test;

import cn.hutool.cache.impl.LRUCache;
import cn.hutool.cache.impl.TimedCache;

/**
 * @author: CS
 * @date: 2022/4/8 下午3:09
 * @description:
 */
public class CacheTest {


    public static void main(String[] args) {
        LRUCacheTest();
    }

    private static void LRUCacheTest() {
        LRUCache lruCache = new LRUCache(3);
        for (int i = 0; i < 4; i++) {
            lruCache.put(i, i);
        }
        lruCache.get(1);
        lruCache.put(4, 4);
        System.out.println(lruCache.toString());
    }


    private static void timedCacheTest() {
        TimedCache timedCache = new TimedCache(6 * 1000);
        timedCache.schedulePrune(1);
        timedCache.put("1", "time");
        long start = System.currentTimeMillis();

        while (!timedCache.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("expired cost:" + (System.currentTimeMillis() - start));

    }
}
