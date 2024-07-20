package com.base.api.entity;

import java.util.Date;

/**
 * 基础实体类
 */
public abstract class BaseEntity extends Base {
    /**
     * 主键
     */
    private String id;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人Id
     */
    private String createUserId;
    /**
     * 更新人Id
     */
    private String  updateUserId;
    /**
     * 更新时间
     *
     */
    private Date  updateDate;

    /**
     * 租户id
     *
     */
    private String tenantId="391312369558487040";

    /**
     * 组件类型
     */
    private String componentType;


    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id='" + id + '\'' +
                ", createDate=" + createDate +
                ", createUserId='" + createUserId + '\'' +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateDate=" + updateDate +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
