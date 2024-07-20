package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;

import java.util.Objects;

/**
 * 组织机构(BaseSysOrg)表实体类
 *
 * @author makejava
 * @since 2021-01-14 09:32:22
 */
public class BaseSysOrg extends BaseEntity {
    //上级机构id
    private String parentId;
    //机构名称
    private String name;
    //简写
    private String simpleName;
    //机构编码
    private String orgCode;
    //0:机构，1：组织，2:公司,3：项目，4：其他
    private String orgType;
    //0:申请，1：经营，2：停业，3：撤销
    private String orgStatus;
    //排序
    private Integer orgSort;
    //0:否（默认），1：是
    private String isDelete;
    //描述
    private String orgDesc;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }

    public Integer getOrgSort() {
        return orgSort;
    }

    public void setOrgSort(Integer orgSort) {
        this.orgSort = orgSort;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseSysOrg)) {
            return false;
        }
        BaseSysOrg that = (BaseSysOrg) o;
        return parentId.equals(that.parentId) &&
                name.equals(that.name) &&
                simpleName.equals(that.simpleName) &&
                orgCode.equals(that.orgCode) &&
                orgType.equals(that.orgType) &&
                orgStatus.equals(that.orgStatus) &&
                orgSort.equals(that.orgSort) &&
                isDelete.equals(that.isDelete) &&
                orgDesc.equals(that.orgDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, name, simpleName, orgCode, orgType, orgStatus, orgSort, isDelete, orgDesc);
    }

    @Override
    public String toString() {
        return "BaseSysOrg{" +
                "parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", simpleName='" + simpleName + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgType='" + orgType + '\'' +
                ", orgStatus='" + orgStatus + '\'' +
                ", orgSort=" + orgSort +
                ", isDelete='" + isDelete + '\'' +
                ", orgDesc='" + orgDesc + '\'' +
                '}';
    }
}
