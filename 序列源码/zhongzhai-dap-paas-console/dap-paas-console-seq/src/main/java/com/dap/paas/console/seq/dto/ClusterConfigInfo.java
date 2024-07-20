package com.dap.paas.console.seq.dto;

import lombok.Data;

/**
 * @className ClusterConfigInfo
 * @description 集群配置信息
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class ClusterConfigInfo {

    /**
     *集群名称
     */
    private String serverName;

    /**
     * sdk名称
     */
    private String sdkName;

    /**
     * 集群下所有server的地址信息，多个地址以逗号隔开
     */
    private String serverUrls;
}
