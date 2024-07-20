package com.dap.paas.console.basic.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DatasourcePro
 **/
@ConfigurationProperties(prefix = "sync.basic")
@Component
@Data
public class BasicInfoReqPo {

    /**
     * 机房
     */
    private String machineRoomUrl;

    /**
     * 地区
     */
    private String cityUrl;

    /**
     * 机器
     */
    private String machineUrl;

    /**
     * 应用
     */
    private String applicationUrl;

    private String userId;
    private String tenantId;

    private String orgId;

    private String deploymentPath;


}