package com.base.sys.api.dto;


import java.io.Serializable;

public class SysOperateDto implements Serializable {
    private static final long serialVersionUID = 6281057477644913048L;
    //权限表的主键id
    private String sysPermissionId;

    //操作点主键Id
    private String operateId;

    //操作点描述
    private String operateDesc;

    //操作点编码
    private String operateCode;

    //资源路径
    private String path;

    //操作点名称
    private String operateName;

    //所属菜单id
    private String menuId;


    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(String sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }
}
