package com.dap.paas.console.basic.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单带有超时的hashmap
 *
 * @author Capt Jack
 * @date 2018/6/29
 */
public final class ExpireConcurrentHashMap<K, V> {

    /**
     * value永久有效
     */
    private static final long FOREVER_AVAILABLE = 0L;

    /**
     * 存储map
     */
    private final Map<K, ExpireEntity<V>> cacheMap;

    public ExpireConcurrentHashMap() {
        cacheMap = new ConcurrentHashMap<>(50);
    }

    /**
     * @param key key
     * @return 存储的对象
     */
    public V get(K key) {
        if (isAvailable(key)) {
            return cacheMap.get(key).getObject();
        }
        cacheMap.remove(key);
        return null;
    }

    /**
     * @param key           key
     * @param value         value
     * @param availableTime 超时时间
     */
    public void put(K key, V value, long availableTime) {
        if (availableTime > 0) {
            cacheMap.put(key, new ExpireEntity<>(value, System.currentTimeMillis() + availableTime));
        } else {
            cacheMap.put(key, new ExpireEntity<>(value, FOREVER_AVAILABLE));
        }
    }

    public void clear() {
        this.cacheMap.clear();
    }

    /**
     * 移除
     *
     * @param key key
     */
    public void remove(K key) {
        cacheMap.remove(key);
    }

    /**
     * @param key key
     * @return 是否存在
     */
    public boolean contains(K key) {
        if (isAvailable(key)) {
            return true;
        }
        cacheMap.remove(key);
        return false;
    }

    /**
     * @param key key
     * @return key是否可用，即存在且在有效期内
     */
    private boolean isAvailable(K key) {
        if (!cacheMap.containsKey(key)) {
            return false;
        }
        ExpireEntity<V> expireEntity = cacheMap.get(key);
        // 永久有效，或者在有效时间内
        return expireEntity.expireTime == FOREVER_AVAILABLE || (cacheMap.get(key).getExpireTime() > System.currentTimeMillis());
    }

    private static final class ExpireEntity<T> {

        private final T object;

        private final long expireTime;

        public ExpireEntity(T object, long expireTime) {
            this.object = object;
            this.expireTime = expireTime;
        }

        public T getObject() {
            return object;
        }

        public long getExpireTime() {
            return expireTime;
        }

    }

}
