package com.dap.paas.console.basic.enums;

public enum ComponentType {

    CACHE("0","缓存中心"),
    MQROCKET("11","消息中心-ROCKET"),
    MQKAFKA("12","消息中心-KAFKA"),
    SEQ("2","序列中心"),
    GOV("3","服务治理"),
    CONFIG("4","配置中心"),
    REGISTER("5","注册中心");

    private final String code;
    private final String description;
    ComponentType(String code, String description){
        this.code = code;
        this.description = description;
    }

    public static ComponentType parse(String code){
        for (ComponentType operateType : ComponentType.values()) {
            if(operateType.code == code) {
                return operateType;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
