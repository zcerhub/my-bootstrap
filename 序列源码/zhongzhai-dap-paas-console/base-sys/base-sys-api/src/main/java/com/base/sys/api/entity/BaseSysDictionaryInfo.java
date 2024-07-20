package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;

/**
 * 字典详情(BaseSysDictionaryInfo)表实体类
 *
 * @author makejava
 * @since 2021-01-21 14:58:01
 */
@SuppressWarnings("serial")
public class BaseSysDictionaryInfo extends BaseEntity {
    //主表唯一标识
    private String dictId;
    //字典名称
    private String dicName;
    //字典值
    private String dicValue;
    //字典描述
    private String dicDesc;

    private Integer orderNum;

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public BaseSysDictionaryInfo() {
        //
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicDesc() {
        return dicDesc;
    }

    public void setDicDesc(String dicDesc) {
        this.dicDesc = dicDesc;
    }


}
