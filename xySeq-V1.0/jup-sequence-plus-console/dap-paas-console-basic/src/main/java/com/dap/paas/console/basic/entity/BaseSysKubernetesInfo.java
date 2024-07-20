package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;

/**
 * (BaseSysKubernetesInfo)实体类
 *
 * @author makejava
 * @since 2023-05-18 11:04:05
 */
public class BaseSysKubernetesInfo extends BaseEntity {

    private String clusterName;

    private String containerClusterName;

    /**
     * 1.mq_rocketmq 2.mq_kafka 3.redis
     */
    private String type;

    private String agentIp;

    private Integer agentPort;
    /**
     * 0:不可用，1:健康
     */
    private String agentState;

    private String images;

    private String deleted;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getContainerClusterName() {
        return containerClusterName;
    }

    public void setContainerClusterName(String containerClusterName) {
        this.containerClusterName = containerClusterName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgentIp() {
        return agentIp;
    }

    public void setAgentIp(String agentIp) {
        this.agentIp = agentIp;
    }

    public Integer getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(Integer agentPort) {
        this.agentPort = agentPort;
    }

    public String getAgentState() {
        return agentState;
    }

    public void setAgentState(String agentState) {
        this.agentState = agentState;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}

