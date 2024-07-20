package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/**
 * 菜单对象
 */
public class BaseSysMenu extends BaseEntity {
    //菜单文本
    private String text;

    //资源路径
    private String path;

    //菜单头标
    private String icon;

    //父菜单id
    private String parentId;

    //同级别菜单排序
    private Integer sort;

    //编码
    private String  code;

    //是否是叶子节点的菜单
    private  String isLeaf;

    //前端路由
    private String routePath;

    //描述
    private String description;

    //应用id
    private String appId;

    //应用名称
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }





    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
