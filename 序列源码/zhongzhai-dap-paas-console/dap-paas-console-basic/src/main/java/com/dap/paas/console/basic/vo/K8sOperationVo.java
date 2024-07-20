package com.dap.paas.console.basic.vo;

import cn.hutool.json.JSONObject;

public class K8sOperationVo {

    private String clusterId;

    private JSONObject context;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public JSONObject getContext() {
        return context;
    }

    public void setContext(JSONObject context) {
        this.context = context;
    }
}
