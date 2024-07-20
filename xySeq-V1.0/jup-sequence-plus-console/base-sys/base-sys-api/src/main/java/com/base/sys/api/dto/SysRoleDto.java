package com.base.sys.api.dto;

import java.util.List;

public class SysRoleDto  {

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

    private String roleId;

    //类型
    private String type;

    //状态
    private String status;

    //同级角色排序
    private Integer roleSort;

    public Integer getRoleSort() {
        return roleSort;
    }

    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //子角色
    private List<SysRoleDto>  children;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<SysRoleDto> getChildren() {
        return children;
    }

    public void setChildren(List<SysRoleDto> children) {
        this.children = children;
    }
}
