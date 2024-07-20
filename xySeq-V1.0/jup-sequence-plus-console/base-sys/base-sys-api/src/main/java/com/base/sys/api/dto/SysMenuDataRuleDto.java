package com.base.sys.api.dto;

import java.util.List;

public class SysMenuDataRuleDto {
    //菜单id
    private String menuId;

    //菜单文本
    private String text;

    //菜单头标
    private String  icon;

    //父菜单id
    private String parentId;

    //同级别菜单排序
    private Integer sort;

    //是否是叶子节点的菜单
    private  String isLeaf;

    //编码
    private String  code;

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private List<SysMenuDataRuleDto> children;

    //数据权限集合
    private List<SysDataRuleDto>  sysDataRuleDtoList;


    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<SysMenuDataRuleDto> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuDataRuleDto> children) {
        this.children = children;
    }

    public List<SysDataRuleDto> getSysDataRuleDtoList() {
        return sysDataRuleDtoList;
    }

    public void setSysDataRuleDtoList(List<SysDataRuleDto> sysDataRuleDtoList) {
        this.sysDataRuleDtoList = sysDataRuleDtoList;
    }
}
