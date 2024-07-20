package com.base.sys.api.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 用户信息的缓存
 */
public class UserInfoCache {

    protected Logger log=Logger.getLogger(getClass().getName());

    private static Map<String , Map<String,Object>> cacheMap = new ConcurrentHashMap<>();

    public static Map<String ,Map<String ,Object>> getCacheMap() {
        return cacheMap;
    }

    public static Map<String ,Object> get(String key) {
        return cacheMap.get(key);
    }


    public static void remove(String key) {
        cacheMap.remove(key);
    }

    public static void clear() {
        cacheMap.clear();
    }

}
