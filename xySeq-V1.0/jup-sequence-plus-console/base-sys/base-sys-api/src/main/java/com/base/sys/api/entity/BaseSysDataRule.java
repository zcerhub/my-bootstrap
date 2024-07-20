package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/**
 *系统数据对象
 *
 */
public class BaseSysDataRule extends BaseEntity {


    // 是否包含下级
    private String isInclude;

     // 所属菜单id
    private String menuId;

    // 数据编码
    private String  code;

    //资源路径
    private String path;

     //名称
    private String name;

    //规则
    private String rule;


    private String type;

    // 对应mybatis的mapper方法id
    private String mapperId;


   private  String  description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMapperId() {
        return mapperId;
    }

    public void setMapperId(String mapperId) {
        this.mapperId = mapperId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIsInclude() {
        return isInclude;
    }

    public void setIsInclude(String isInclude) {
        this.isInclude = isInclude;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
