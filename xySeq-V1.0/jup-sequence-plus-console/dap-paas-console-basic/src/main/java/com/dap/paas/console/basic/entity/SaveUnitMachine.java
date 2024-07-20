package com.dap.paas.console.basic.entity;

import java.util.List;

public class SaveUnitMachine {
    private String unitId;
    private List<String> idList;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
