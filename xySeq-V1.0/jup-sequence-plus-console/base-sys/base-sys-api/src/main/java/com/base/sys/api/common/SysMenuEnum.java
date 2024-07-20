package com.base.sys.api.common;

/**
 * 菜单的枚举
 */
public enum SysMenuEnum {

    LEAF("Y", "叶子节点"),
    NO_LEAF("N", "非叶子节点");

    private String code;
    private String msg;

    SysMenuEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }


}
