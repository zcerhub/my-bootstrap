package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/**
 * 用户角色关联实体
 */
public class BaseSysUserRole extends BaseEntity {

    //角色id
    private String roleId;

    //用户id
    private String userId;

    private String createOrgId;

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
