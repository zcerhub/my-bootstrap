package com.base.sys.api.dto;

import java.io.Serializable;
import java.util.List;

public class SysOperateSign implements Serializable {
    private static final long serialVersionUID = -8400244621446682022L;
    private String  routePath;


    private List<String> codeList;

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }
}
