package com.base.sys.api.common;


/**
 * @author keriezhang
 * @date 2021/04/15
 * 数据权限的枚举
 */
public enum DataAuthEnum {
    /**
     *  DATA_SCOPE_ALL  数据权限的枚举
     */
    DATA_SCOPE_ALL("data_scope_all", "所有数据权限"),
    /**
     *  DATA_SCOPE_CUSTOM  数据权限的枚举
     */
    DATA_SCOPE_CUSTOM("data_scope_custom", "自定义数据权限"),
    /**
     *  DATA_SCOPE_ORG  数据权限的枚举
     */
    DATA_SCOPE_ORG("data_scope_org", "部门数据权限"),
    /**
     *  DATA_SCOPE_ORG_AND_CHILD  数据权限的枚举
     */
    DATA_SCOPE_ORG_AND_CHILD("data_scope_org_and_child", "部门及以下数据权限"),
    /**
     *  DATA_SCOPE_SELF  数据权限的枚举
     */
    DATA_SCOPE_SELF("data_scope_self", "仅本人数据权限");

    private String code;

    private String msg;

    DataAuthEnum(String code, String msg) {
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
