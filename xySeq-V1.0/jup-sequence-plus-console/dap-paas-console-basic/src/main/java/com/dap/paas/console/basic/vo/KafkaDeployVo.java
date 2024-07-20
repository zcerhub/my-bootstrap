package com.dap.paas.console.basic.vo;

import cn.hutool.json.JSONObject;

public class KafkaDeployVo {

    private String clusterId;

    private JSONObject zkDeployInfo;

    private JSONObject brokerDeployInfo;

    private String zkAddrs;

    /*
     * 支持虚机网络 pod是否使用主机网络，取值：true，false，默认false
     */
    private Boolean hostNetwork;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public JSONObject getZkDeployInfo() {
        return zkDeployInfo;
    }

    public void setZkDeployInfo(JSONObject zkDeployInfo) {
        this.zkDeployInfo = zkDeployInfo;
    }

    public JSONObject getBrokerDeployInfo() {
        return brokerDeployInfo;
    }

    public void setBrokerDeployInfo(JSONObject brokerDeployInfo) {
        this.brokerDeployInfo = brokerDeployInfo;
    }

    public String getZkAddrs() {
        return zkAddrs;
    }

    public void setZkAddrs(String zkAddrs) {
        this.zkAddrs = zkAddrs;
    }

    public Boolean getHostNetwork() {
        return hostNetwork;
    }

    public void setHostNetwork(Boolean hostNetwork) {
        this.hostNetwork = hostNetwork;
    }
}
