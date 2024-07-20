package com.dap.paas.console.common.enums;

/**
 * 主机所属系统枚举
 *
 */
public enum SystemEnum {
    ALL("all"),
    CACHE("CacheService"),
    MQ("MessageService"),
    CONFIG("ConfigCenter"),
    APP("Application");

    SystemEnum(String name) {
        this.name = name;
    }
    String name;
}
