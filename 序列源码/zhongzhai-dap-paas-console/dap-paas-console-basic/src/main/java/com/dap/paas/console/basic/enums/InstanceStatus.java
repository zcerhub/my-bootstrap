package com.dap.paas.console.basic.enums;

/**
 * 服务状态
 */
public enum InstanceStatus {
    INIT((byte)0,"初始化"),
    RUNNING((byte)1,"启动"),
    STOPPED((byte)2,"停止");
    private final byte code;
    private final String description;
    InstanceStatus(byte code, String description){
        this.code = code;
        this.description = description;
    }

    public static InstanceStatus parse(byte code){
        for (InstanceStatus type : InstanceStatus.values()) {
            if(type.code == code) {
                return type;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
