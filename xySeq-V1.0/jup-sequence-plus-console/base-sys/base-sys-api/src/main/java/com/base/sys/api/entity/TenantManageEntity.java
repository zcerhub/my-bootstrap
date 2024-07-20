package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;


public class TenantManageEntity extends BaseEntity {

    private String tenantName;
    private String tenantCompany;
    private String tenantMail;
    private String tenantPhone;
    private String tenantRealmName;
    private String tenantStatus;
    private String authStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String tenantCode;



    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantCompany() {
        return tenantCompany;
    }

    public void setTenantCompany(String tenantCompany) {
        this.tenantCompany = tenantCompany;
    }

    public String getTenantMail() {
        return tenantMail;
    }

    public void setTenantMail(String tenantMail) {
        this.tenantMail = tenantMail;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    public String getTenantRealmName() {
        return tenantRealmName;
    }

    public void setTenantRealmName(String tenantRealmName) {
        this.tenantRealmName = tenantRealmName;
    }

    public String getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(String tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
