package com.dap.paas.console.basic.enums;

public enum OperateType {
    CLIENT_INSTALL((byte)1,"客户端安装"),
    CLIENT_START((byte)2,"客户端启动"),
    CLIENT_STOP((byte)3,"客户端停止"),
    CLIENT_UNINSTALL((byte)4,"客户端卸载");
    private final byte code;
    private final String description;
    OperateType(byte code, String description){
        this.code = code;
        this.description = description;
    }

    public static OperateType parse(byte code){
        for (OperateType operateType : OperateType.values()) {
            if(operateType.code == code) {
                return operateType;
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
