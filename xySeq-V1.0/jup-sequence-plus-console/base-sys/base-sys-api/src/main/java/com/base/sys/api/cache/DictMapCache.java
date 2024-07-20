package com.base.sys.api.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 数据字典的缓存
 */
public class DictMapCache {

    protected Logger log=Logger.getLogger(getClass().getName());
    private static Map<String , Map<String,String>> cacheMap = new ConcurrentHashMap<> ();

    public static void destoryCacheMap() {
        cacheMap = null;
    }

    public static Map<String , Map<String,String>> getCacheMap() {
        return cacheMap;
    }

    public static void set(String key, Map<String,String>  values) {
        cacheMap.put(key, values);
    }

    public static Map<String,String> get(String key) {
        return  cacheMap.get(key);
    }

    public static void remove(Object key) {
        cacheMap.remove(key);
    }

    public static void clear() {
        cacheMap.clear();
    }

}
