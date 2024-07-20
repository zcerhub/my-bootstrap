package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysOperate;

import java.util.List;

/**
 * 菜单下包含的操作点和按钮
 */
public class OperateAndDataRuleDto {

    private String menuId;

    private List<BaseSysOperate> operateList;

    private List<BaseSysDataRule> dataRuleList;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public List<BaseSysOperate> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<BaseSysOperate> operateList) {
        this.operateList = operateList;
    }

    public List<BaseSysDataRule> getDataRuleList() {
        return dataRuleList;
    }

    public void setDataRuleList(List<BaseSysDataRule> dataRuleList) {
        this.dataRuleList = dataRuleList;
    }
}
