package com.dap.paas.console.basic.enums;

import lombok.Getter;

@Getter
public enum ComponentEnum {
    CACHE("缓存中心"), ROCKETMQ("RocketMQ消息队列"), KAFKA("Kafka消息队列");

    private String desc;

    ComponentEnum(String desc) {
        this.desc = desc;
    }

    //校验是否是枚举中的值
    public static boolean isExist(String name) {
        for (ComponentEnum componentEnum : ComponentEnum.values()) {
            if (componentEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
