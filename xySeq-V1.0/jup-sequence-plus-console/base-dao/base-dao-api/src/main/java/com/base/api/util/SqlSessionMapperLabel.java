package com.base.api.util;

public enum SqlSessionMapperLabel {

    QueryList("queryList","查询列表对象"),
    QueryListByPo("queryListByPo","根据对象查询列表对象"),
    QueryPage("queryPage","查询分页对象"),
    GetObjectByMap("getObjectByMap","传参Map查询单个对象"),
    GetObjectByEntity("getObjectByEntity","传参实体查询单个对象"),
    GetObjectById("getObjectById","传参Id查询单个对象"),
    DelObjectById("delObjectById","删除单个对象"),
    SaveObject("saveObject","保存对象"),
    UpdateObject("updateObject","更新对象"),
    UpdateStatus("updateStatus","根据对象更新状态"),
    DelObjectByIds("delObjectByIds","批量删除对象"),
    InsertBatch("insertBatch","批量插入"),
    DeleteAttributeData("deleteAttributeData","根据字段删除数据"),
    GetIds("getIds","无条件查询ID集合"),
    SelectTotal("selectTotal","根据Map查询数据条数"),
    QueryListByMap("queryListByMap","根据Map查询数据条数"),
    QueryListByIds("queryListByIds","根据ids批量查询");




    /**
     * 标识
     */
    private String name;
    /**
     * 信息
     */
    private String code;

    SqlSessionMapperLabel(String code,String name) {
        this.name=name;
        this.code=code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
