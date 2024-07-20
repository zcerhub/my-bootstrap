package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/***
 * 授权表对象
 */

public class BaseSysPermission extends BaseEntity {
    // 角色id
    private String roleId;

    //菜单id
    private String menuId;

    //操作点id
    private String operateId;

    //应用id
    private String appId;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }


}
