package com.dap.paas.console.basic.vo;

public class KafkaVo {

    private String clusterId;

    private String zkAddrs;

    private String brokerAddrs;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getZkAddrs() {
        return zkAddrs;
    }

    public void setZkAddrs(String zkAddrs) {
        this.zkAddrs = zkAddrs;
    }

    public String getBrokerAddrs() {
        return brokerAddrs;
    }

    public void setBrokerAddrs(String brokerAddrs) {
        this.brokerAddrs = brokerAddrs;
    }
}
