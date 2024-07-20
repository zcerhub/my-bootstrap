package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/**
 * 数据权限对象
 */
public class BaseSysPermissionData extends BaseEntity {


    // 系统数据实体id
    private String dataRuleId;

    // 角色id
    private String roleId;

    private String appId;

    private String menuId;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDataRuleId() {
        return dataRuleId;
    }

    public void setDataRuleId(String dataRuleId) {
        this.dataRuleId = dataRuleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
