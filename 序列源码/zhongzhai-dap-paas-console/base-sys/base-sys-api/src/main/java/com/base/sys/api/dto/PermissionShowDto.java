package com.base.sys.api.dto;

import java.util.List;

public class PermissionShowDto {

    private List<PermissionTreeDto> allShow;


    private List<PermissionTreeDto> dataRuleShow;


    private List<PermissionTreeDto> operateList;


    private List<PermissionTreeDto> dataRuleList;

    private String roleId;

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

    public List<PermissionTreeDto> getAllShow() {
        return allShow;
    }

    public void setAllShow(List<PermissionTreeDto> allShow) {
        this.allShow = allShow;
    }

    public List<PermissionTreeDto> getDataRuleShow() {
        return dataRuleShow;
    }

    public void setDataRuleShow(List<PermissionTreeDto> dataRuleShow) {
        this.dataRuleShow = dataRuleShow;
    }

    public List<PermissionTreeDto> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<PermissionTreeDto> operateList) {
        this.operateList = operateList;
    }

    public List<PermissionTreeDto> getDataRuleList() {
        return dataRuleList;
    }

    public void setDataRuleList(List<PermissionTreeDto> dataRuleList) {
        this.dataRuleList = dataRuleList;
    }
}
