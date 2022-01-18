package com.cs.common.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class DefaultRedisLock {

    public static final long MIN = 50;
    public static final int TWO = 2;
    private static final ConcurrentHashMap<Long, String> THREAD_KEY_MAP = new ConcurrentHashMap<>();
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisLockProperty redisLockProperty;

    public DefaultRedisLock(StringRedisTemplate redisTemplate, RedisLockProperty redisLockProperty) {
        this.redisTemplate = redisTemplate;
        this.redisLockProperty = redisLockProperty;
    }

    /**
     * redis 设值
     *
     * @param key
     * @return
     */
    private boolean add(String key, String value, Long timeout) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                boolean success = redisConnection.setNX(serializer.serialize(key), serializer.serialize(value));
                if (success) {
                    redisConnection.pExpire(serializer.serialize(key), timeout);
                }
                return success;
            }
        });
    }

    /**
     * redis 删除值
     *
     * @param key
     * @return
     */
    private boolean del(String key, String value) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                String val = serializer.deserialize(redisConnection.get(serializer.serialize(key)));
                if (!value.equals(val)) {
                    return false;
                }
                redisConnection.del(serializer.serialize(key));
                return true;
            }
        });
    }

    /**
     * 加锁，失败抛出异常
     *
     * @param key redis value
     */
    public void lock(String key) {
        String loclKey = getLockKey(key);
        String uuid = UUID.randomUUID().toString();
        long id = Thread.currentThread().getId();

        if (THREAD_KEY_MAP.containsKey(id)) {
            throw new RedisLockException("Current thread has acquired the lock.");
        }

        try {
            THREAD_KEY_MAP.put(id, uuid);
            boolean success = add(loclKey, uuid, Long.parseLong(redisLockProperty.getLiveTime()));

            if (!success) {
                throw new RedisLockException(redisLockProperty.getDefaultError());
            }
        } catch (Exception e) {
            THREAD_KEY_MAP.remove(id);
            throw e;
        }
    }

    /**
     * 尝试加锁，返回加锁结果
     *
     * @param key
     * @return
     */
    public boolean tryLock(String key) {
        return tryLock(key, 0L);
    }

    /**
     * 尝试固定时间内加锁，返回加锁结果，单位毫秒
     *
     * @param key
     * @param time
     * @return
     */
    public boolean tryLock(String key, long time) {
        String lockKey = getLockKey(key);
        String uuid = UUID.randomUUID().toString();
        long id = Thread.currentThread().getId();
        long timeout = Long.parseLong(redisLockProperty.getLiveTime());

        if (THREAD_KEY_MAP.containsKey(id)) {
            return false;
        }

        try {
            THREAD_KEY_MAP.put(id, uuid);

            boolean success = add(lockKey, uuid, timeout);

            if (!success) {
                long max = timeout;
                long min = MIN;
                if (time < min) {
                    if (time > 0) {
                        TimeUnit.MILLISECONDS.sleep(time);
                        success = add(lockKey, uuid, timeout);
                    }
                } else {
                    max = Math.min(time, max);
                    while (max > 0) {
                        TimeUnit.MILLISECONDS.sleep(min);
                        success = add(lockKey, uuid, timeout);
                        if (success) {
                            break;
                        }
                        max = max - min;
                        min = TWO * min;
                        min = Math.min(max, min);
                    }
                }
            }

            return success;
        } catch (Exception e) {
            THREAD_KEY_MAP.remove(id);
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param key
     */
    public void unlock(String key) {
        String lockKey = getLockKey(key);
        long id = Thread.currentThread().getId();
        if (!THREAD_KEY_MAP.containsKey(id)) {
            return;
        }

        try {
            String uuid = THREAD_KEY_MAP.get(id);
            del(lockKey, uuid);
        } finally {
            THREAD_KEY_MAP.remove(id);
        }
    }

    private String getLockKey(String key) {
        return redisLockProperty.getPrefix() + key;
    }
}
