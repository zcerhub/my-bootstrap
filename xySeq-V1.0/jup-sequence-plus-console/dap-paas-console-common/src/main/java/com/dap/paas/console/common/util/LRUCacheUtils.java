package com.dap.paas.console.common.util;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * LRU容器
 *
 */
public class LRUCacheUtils<K, V> extends AbstractMap<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int MAX_CACHE_SIZE;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;
    LinkedHashMap<K, V> map;

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    public LRUCacheUtils(int cacheSize) {
        MAX_CACHE_SIZE = cacheSize;
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTOR) + 1;
        map = new LinkedHashMap(capacity, DEFAULT_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    public LRUCacheUtils() {
        MAX_CACHE_SIZE = 100000;
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTOR) + 1;
        map = new LinkedHashMap(capacity, DEFAULT_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }
    @Override
    public synchronized V put(K key, V value) {
        return map.put(key, value);
    }
    @Override
    public synchronized V get(Object key) {
        return map.get(key);
    }
    @Override
    public synchronized V remove(Object key) {
        return map.remove(key);
    }
    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
    @Override
    public synchronized int size() {
        return map.size();
    }
    @Override
    public synchronized void clear() {
        map.clear();
    }
    @Override
    public synchronized boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : map.entrySet()) {
            sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}
