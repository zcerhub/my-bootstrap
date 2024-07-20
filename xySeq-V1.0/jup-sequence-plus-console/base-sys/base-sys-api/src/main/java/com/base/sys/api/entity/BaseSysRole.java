package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/***
 * 角色对象
 */
public class BaseSysRole extends BaseEntity {
    //角色编码
    private String code;

    //角色名称
    private String name;

    //角色描述
    private String description;

    // 角色关键字
    private String remark;

    //角色父id
    private String parentId;

    //角色类型
    private String type;

    //角色状态 1启用  0不启用
    private String status;

    //角色排序
    private Integer roleSort;

    public Integer getRoleSort() {
        return roleSort;
    }

    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
