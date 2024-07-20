package com.base.sys.api.common;

public enum PermissionTypeEnum {

    MENU_TYPE("1", "菜单类型"),
    OPERATE_TYPE("2", "操作点类型"),
    DATA_RULE_TYPE("3", "数据规则类型"),
    APP_TYPE("4", "应用类型");

    private String code;
    private String msg;


    PermissionTypeEnum(String code, String msg) {
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
