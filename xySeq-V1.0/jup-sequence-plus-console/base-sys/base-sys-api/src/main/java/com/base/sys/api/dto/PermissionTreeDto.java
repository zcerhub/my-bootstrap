package com.base.sys.api.dto;

import java.util.List;

public class PermissionTreeDto {

    //父节点id
    private String parentId;

    //主键id
    private String id;

    //名称
    private String name;

    // 类型
    private String type;

    //权限表的主键id
    private String sysPermissionId;

    //数据授权表的主键
    private String dataPermissionId;

    //是否显示选中
    private boolean isChecked;

    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    private List<PermissionTreeDto> children;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(String sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
    }

    public String getDataPermissionId() {
        return dataPermissionId;
    }

    public void setDataPermissionId(String dataPermissionId) {
        this.dataPermissionId = dataPermissionId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<PermissionTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionTreeDto> children) {
        this.children = children;
    }
}
