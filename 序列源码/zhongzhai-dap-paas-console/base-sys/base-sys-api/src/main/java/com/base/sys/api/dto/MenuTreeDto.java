package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.api.entity.BaseSysPermissionData;

import java.util.List;

public class MenuTreeDto {
    String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    List<TreeNodeData> menuList;
    List<TreeNodeData> operateList;
    List<String> checkMenu;
    List<String> checkOprate;
    List<TreeNodeData> dataList;
    List<String> checkData;
    List<String> checkDataMenu;


    public List<String> getCheckDataMenu() {
        return checkDataMenu;
    }

    public void setCheckDataMenu(List<String> checkDataMenu) {
        this.checkDataMenu = checkDataMenu;
    }

    public List<TreeNodeData> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<TreeNodeData> menuList) {
        this.menuList = menuList;
    }

    public List<TreeNodeData> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<TreeNodeData> operateList) {
        this.operateList = operateList;
    }

    public List<TreeNodeData> getDataList() {
        return dataList;
    }

    public void setDataList(List<TreeNodeData> dataList) {
        this.dataList = dataList;
    }

    public List<String> getCheckData() {
        return checkData;
    }

    public void setCheckData(List<String> checkData) {
        this.checkData = checkData;
    }

    public List<String> getCheckMenu() {
        return checkMenu;
    }

    public void setCheckMenu(List<String> checkMenu) {
        this.checkMenu = checkMenu;
    }

    public List<String> getCheckOprate() {
        return checkOprate;
    }

    public void setCheckOprate(List<String> checkOprate) {
        this.checkOprate = checkOprate;
    }


}
