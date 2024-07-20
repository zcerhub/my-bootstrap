package com.dap.paas.console.basic.enums;

import java.util.*;

public enum ModuleEnum {
    REDIS, ROCKRTMQ_NAME_SERVER, ROCKRTMQ_NAME_BROKER, KAFKA;

    private final static List<ModuleEnum> CACHE_MODULES = Collections.singletonList(REDIS);

    private final static List<ModuleEnum> ROCKETMQ_MODULES =
            Arrays.asList(ROCKRTMQ_NAME_SERVER, ROCKRTMQ_NAME_BROKER);

    private final static List<ModuleEnum> KAFKA_MODULES = Arrays.asList(KAFKA);

    private final static Map<String, List<ModuleEnum>> MODULE_MAP = new HashMap<String, List<ModuleEnum>>(){
        {
            put(ComponentEnum.CACHE.name(), CACHE_MODULES);
            put(ComponentEnum.ROCKETMQ.name(), ROCKETMQ_MODULES);
            put(ComponentEnum.KAFKA.name(), KAFKA_MODULES);
        }
    };

    /**
     * 根据组件类型获取模块列表
     * @param componentType 组件类型
     * @return 模块列表
     */
    public static List<ModuleEnum> getCacheModules(String componentType) {
        return MODULE_MAP.get(componentType);
    }

    //校验是否是枚举中的值
    public static boolean isExist(String name) {
        for (ModuleEnum moduleEnum : values()) {
            if (moduleEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
