package com.dap.paas.console.basic.enums;

public enum ClientEnv {
    DEV((byte)1,"dev"),
    SIT((byte)2,"sit"),
    UAT((byte)3,"uat"),
    PROD((byte)4,"prod");

    private final byte code;
    private final String description;
    ClientEnv(byte code, String description){
        this.code = code;
        this.description = description;
    }

    public static ClientEnv parse(byte code){
        for (ClientEnv operateType : ClientEnv.values()) {
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
