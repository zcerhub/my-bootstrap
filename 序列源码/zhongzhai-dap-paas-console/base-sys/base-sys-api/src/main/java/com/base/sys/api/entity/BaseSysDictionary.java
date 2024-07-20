package com.base.sys.api.entity;

import com.base.api.entity.BaseEntity;

/**
 * 系统字典表(BaseSysDictionary)表实体类
 *
 * @author makejava
 * @since 2021-01-21 14:56:59
 */
@SuppressWarnings("serial")
public class BaseSysDictionary extends BaseEntity {
    //字典编码
    private String dicCode;
    //字典名称
    private String dicName;
    //描述
    private String dicDesc;
    //所属分类
    private String cateCode;



    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicDesc() {
        return dicDesc;
    }

    public void setDicDesc(String dicDesc) {
        this.dicDesc = dicDesc;
    }

    public String getCateCode() {
        return cateCode;
    }

    public void setCateCode(String cateCode) {
        this.cateCode = cateCode;
    }

}
