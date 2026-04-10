/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.libcommon.utils;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: Wcke
 * @description: Redis工具类
 * @datetime: 2024-06-13 15:45
 */
public class BQRedisUtils<T> {
    static private RedisTemplate redisTemplate;
    /**
     * 初始化
     * 生命周期只初始化一次，在Bean装载时初始化
     */
    static public void init(RedisTemplate redisTemplate) {
        if (Objects.isNull(BQRedisUtils.redisTemplate)) {
            BQRedisUtils.redisTemplate = redisTemplate;
        }
    }

    /**
     * 设置一直生效的值
     * @param key
     * @param v
     * @param <T>
     */
    static public <T> void set(final String key, final T v) {
        redisTemplate.opsForValue().set(key, v);
    }

    /**
     * 设置有存活时间的值
     * @param key
     * @param v
     * @param timeout
     * @param timeUnit
     * @param <T>
     */
    static public <T> void set(final String key, final T v, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, v, timeout, timeUnit);
    }

    /**
     * 设置存活时间
     * @param key
     * @param timeout
     * @return
     */
    static public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置存活时间
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    static public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取值
     * @param key
     * @param <T>
     * @return
     */
    static public <T> T get(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除值
     * @param key
     * @return
     */
    static public boolean delete(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除值
     * @param collection
     * @return
     */
    static public long delete(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 添加值
     * @param key
     * @param v
     * @param <T>
     * @return
     */
    static public <T> long set(final String key, final List<T> v) {
        Long count = redisTemplate.opsForList().rightPushAll(key, v);
        return count == null ? 0 : count;
    }

    /**
     * 获取值
     * @param key
     * @param <T>
     * @return
     */
    static public <T> List<T> getList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 添加值
     * @param key
     * @param v
     * @param <T>
     * @return
     */
    static public <T> BoundSetOperations<String, T> set(final String key, final Set<T> v) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = v.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获取值
     * @param key
     * @param <T>
     * @return
     */
    static public <T> Set<T> getSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 添加值
     * @param key
     * @param v
     * @param <T>
     */
    static public <T> void set(final String key, final Map<String, T> v) {
        if (v != null) {
            redisTemplate.opsForHash().putAll(key, v);
        } else {
            delete(key);
        }
    }

    /**
     * 获取值
     * @param key
     * @param <T>
     * @return
     */
    static public <T> Map<String, T> getMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 添加值
     * @param key
     * @param hk
     * @param v
     * @param <T>
     */
    static public <T> void set(final String key, final String hk, final T v) {
        redisTemplate.opsForHash().put(key, hk, v);
    }

    /**
     * 获取值
     * @param key
     * @param hk
     * @param <T>
     * @return
     */
    static public <T> T getMapVal(final String key, final String hk) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hk);
    }

    /**
     * 添加值
     * @param key
     * @param hk
     * @param v
     */
    static public void increment(String key, String hk, int v) {
        redisTemplate.opsForHash().increment(key, hk, v);
    }

    /**
     * 删除值
     * @param key
     * @param hk
     */
    static public void deleteMapVal(final String key, final String hk) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hk);
    }

    /**
     * 获取值
     * @param key
     * @param hks
     * @param <T>
     * @return
     */
    static public <T> List<T> getMultiMapVal(final String key, final Collection<Object> hks) {
        return redisTemplate.opsForHash().multiGet(key, hks);
    }

    /**
     * 获取值
     * @param pattern
     * @return
     */
    static public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }
}
