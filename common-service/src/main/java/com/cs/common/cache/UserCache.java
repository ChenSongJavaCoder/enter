package com.cs.common.cache;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.cs.common.thread.ThreadExecutor;
import com.cs.common.util.UUIDUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: CS
 * @date: 2022/1/19 下午3:36
 * @description:
 */
public class UserCache {
    /**
     * 缓存的队列数据失效后，队列的长度并不是立即减少
     * 可以将缓存队列数据转移到另一个数据结构中进行比对
     */
    static Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .initialCapacity(6)
            .maximumSize(6)
            .build();

    /**
     * 获取不可用的企查查账户
     *
     * @return
     */
    static Set<Object> unAvailableAccount() {
        return CACHE.asMap().values().stream().collect(Collectors.toSet());
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        CACHE.put("a", UUIDUtil.uuid32());
        TimeUnit.SECONDS.sleep(2);
        CACHE.put("b", UUIDUtil.uuid32());

        ThreadExecutor.execute(() -> {
            do {
                System.out.println(CACHE.getIfPresent("a"));
                System.out.println(JSONUtil.toJsonStr(CACHE.stats()));
                System.out.println(atomicInteger.incrementAndGet() + "-" + CACHE.asMap().size() + JSONUtil.toJsonStr(CACHE.asMap()));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (CollectionUtil.isNotEmpty(unAvailableAccount()));
            System.exit(0);
        });

    }
}
