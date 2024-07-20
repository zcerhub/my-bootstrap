package com.dap.paas.console.basic.vo;

import cn.hutool.json.JSONObject;

public class RocketmqDeployVo {

    private String clusterId;

    private JSONObject brokerDeployInfo;

    private JSONObject nameserverDeployInfo;

    private String nameServerAddrs;

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

    public JSONObject getBrokerDeployInfo() {
        return brokerDeployInfo;
    }

    public void setBrokerDeployInfo(JSONObject brokerDeployInfo) {
        this.brokerDeployInfo = brokerDeployInfo;
    }

    public JSONObject getNameserverDeployInfo() {
        return nameserverDeployInfo;
    }

    public void setNameserverDeployInfo(JSONObject nameserverDeployInfo) {
        this.nameserverDeployInfo = nameserverDeployInfo;
    }

    public String getNameServerAddrs() {
        return nameServerAddrs;
    }

    public void setNameServerAddrs(String nameServerAddrs) {
        this.nameServerAddrs = nameServerAddrs;
    }

    public Boolean getHostNetwork() {
        return hostNetwork;
    }

    public void setHostNetwork(Boolean hostNetwork) {
        this.hostNetwork = hostNetwork;
    }
}
