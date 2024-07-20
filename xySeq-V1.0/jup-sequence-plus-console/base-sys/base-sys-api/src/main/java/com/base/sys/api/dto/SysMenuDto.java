package com.base.sys.api.dto;


import java.io.Serializable;
import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/17
 */
public class SysMenuDto implements Serializable {

    private static final long serialVersionUID = -6223302734032140778L;
    //菜单id
    private String id;

    //菜单id
    private String menuId;

    //菜单文本
    private String text;

    //资源路径
    private String path;

    //菜单头标
    private String menuIcon;

    //父菜单id
    private String parentId;

    //菜单级别
    private Integer menuLevel;

    //同级别菜单排序
    private Integer sort;

    //编码
    private String code;

    //是否叶子节点
    private String isLeaf;

    // 新的parentId
    private String newParentId;

    //权限表的主键id
    private String sysPermissionId;

    //前端路由
    private String routePath;


    //操作点集合
    private transient List<SysOperateDto> sysOperateDtoList;

    //数据规则
    private List<SysDataRuleDto> sysDataRuleDtoList;

    //子菜单
    private List<SysMenuDto> children;

    private String icon;

    private String description;

    private String appId;

    private  String appName;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SysDataRuleDto> getSysDataRuleDtoList() {
        return sysDataRuleDtoList;
    }

    public void setSysDataRuleDtoList(List<SysDataRuleDto> sysDataRuleDtoList) {
        this.sysDataRuleDtoList = sysDataRuleDtoList;
    }

    public List<SysOperateDto> getSysOperateDtoList() {
        return sysOperateDtoList;
    }

    public void setSysOperateDtoList(List<SysOperateDto> sysOperateDtoList) {
        this.sysOperateDtoList = sysOperateDtoList;
    }

    public String getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(String sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
    }

    public String getNewParentId() {
        return newParentId;
    }

    public void setNewParentId(String newParentId) {
        this.newParentId = newParentId;
    }



    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }



    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SysMenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuDto> children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
