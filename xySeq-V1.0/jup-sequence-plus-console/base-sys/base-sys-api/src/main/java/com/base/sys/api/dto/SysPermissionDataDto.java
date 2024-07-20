package com.base.sys.api.dto;

public class SysPermissionDataDto {

    // 系统数据实体id
    private String dataRuleId;

    // 角色id
    private String roleId;

    //资源路径
    private String path;

    //名称
    private String name;

    //规则
    private String rule;


    private Boolean isAccessible;


    private String dataCode;

    private String dataName;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataRuleId() {
        return dataRuleId;
    }

    public void setDataRuleId(String dataRuleId) {
        this.dataRuleId = dataRuleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Boolean getAccessible() {
        return isAccessible;
    }

    public void setAccessible(Boolean accessible) {
        isAccessible = accessible;
    }
}
