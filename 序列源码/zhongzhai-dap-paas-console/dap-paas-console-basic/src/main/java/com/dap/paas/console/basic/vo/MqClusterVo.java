package com.dap.paas.console.basic.vo;

public class MqClusterVo {

    private String clusterId;

    private String context;

    private String brokerAddrs;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getBrokerAddrs() {
        return brokerAddrs;
    }

    public void setBrokerAddrs(String brokerAddrs) {
        this.brokerAddrs = brokerAddrs;
    }
}
