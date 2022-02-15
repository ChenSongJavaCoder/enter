/**
 * @author: CS
 * @date: 2022/1/19 下午3:20
 * @description: 缓存的数据结构实现
 * 1、本地数据缓存
 * @see com.google.common.cache.Cache
 * @see com.google.common.cache.CacheBuilder
 * @see com.cs.es.binlog.cache.MemoryCache
 * 2、远端数据缓存
 * redis、数据库
 *
 * 缓存失效
 * 1、相同的失效时间
 * @see com.google.common.cache.CacheBuilder
 * 2、不同的失效时间
 * @see java.util.concurrent.DelayQueue
 *
 * LRU(least recently used)
 * @see cn.hutool.cache.impl.LRUCache
 * @see cn.hutool.cache.CacheUtil
 */
package com.cs.common.cache;