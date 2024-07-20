package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysDataRule;


public class SysDataRuleDto extends BaseSysDataRule {

    //数据授权id
    private String permissionDataId;

    private String dataRuleId;


    public String getDataRuleId() {
        return dataRuleId;
    }

    public void setDataRuleId(String dataRuleId) {
        this.dataRuleId = dataRuleId;
    }

    public String getPermissionDataId() {
        return permissionDataId;
    }

    public void setPermissionDataId(String permissionDataId) {
        this.permissionDataId = permissionDataId;
    }
}
