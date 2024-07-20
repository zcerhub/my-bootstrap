package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;

/**
 * (BaseSysUserOrg)表实体类
 *
 * @author makejava
 * @since 2021-01-14 09:33:22
 */
public class BaseSysUserOrg extends BaseEntity {

    //机构id
    private String orgId;

    //用户id
    private String userId;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
