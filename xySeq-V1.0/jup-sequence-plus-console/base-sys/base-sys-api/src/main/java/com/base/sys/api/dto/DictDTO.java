package com.base.sys.api.dto;

import com.base.sys.api.entity.BaseSysDictionaryInfo;

import java.util.List;

/**
 * 系统字典
 * yanfeng.ma
 * 2021年1月21日
 */
public class DictDTO {
    //主表唯一标识
    private  String id;

    private List<BaseSysDictionaryInfo>  baseSysDictionaryInfoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BaseSysDictionaryInfo> getBaseSysDictionaryInfoList() {
        return baseSysDictionaryInfoList;
    }

    public void setBaseSysDictionaryInfoList(List<BaseSysDictionaryInfo> baseSysDictionaryInfoList) {
        this.baseSysDictionaryInfoList = baseSysDictionaryInfoList;
    }
}
