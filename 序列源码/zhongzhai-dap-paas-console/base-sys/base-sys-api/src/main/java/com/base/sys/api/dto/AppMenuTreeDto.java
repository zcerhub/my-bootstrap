package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysApp;

import java.util.List;

public class AppMenuTreeDto {
    List<TreeNodeData> appList;

    List<MenuTreeDto> menuTreeList;

    public List<TreeNodeData> getAppList() {
        return appList;
    }

    public void setAppList(List<TreeNodeData> appList) {
        this.appList = appList;
    }

    public List<MenuTreeDto> getMenuTreeList() {
        return menuTreeList;
    }

    public void setMenuTreeList(List<MenuTreeDto> menuTreeList) {
        this.menuTreeList = menuTreeList;
    }
}
