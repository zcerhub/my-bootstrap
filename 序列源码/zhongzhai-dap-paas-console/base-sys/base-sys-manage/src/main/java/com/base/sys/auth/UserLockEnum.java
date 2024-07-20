package com.base.sys.auth;

public enum UserLockEnum {
    NO_LOCK("0","未锁定"),
    IS_LOCK("1","已经锁定");
    private String code;
    private String desc;



    UserLockEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
