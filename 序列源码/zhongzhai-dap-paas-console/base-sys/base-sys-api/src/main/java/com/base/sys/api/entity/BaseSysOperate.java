package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;


/***
 * 操作点对象
 */
public class BaseSysOperate extends BaseEntity {

    //操作点描述
    private String description;

    //操作点编码
    private String code;

    //资源路径
    private String path;

    //操作点名称
    private String name;

    //所属菜单id
    private String menuId;

    //请求类型(GET,POST...)
    private String type;

    //按钮是否隐藏（0代表隐藏，1代表不隐藏）
    private String isHidden;

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
