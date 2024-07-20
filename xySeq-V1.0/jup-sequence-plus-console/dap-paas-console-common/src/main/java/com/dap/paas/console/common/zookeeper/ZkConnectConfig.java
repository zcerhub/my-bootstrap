package com.dap.paas.console.common.zookeeper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: zk配置
 * @Author: luotong
 * @Date: 2019/7/23 16:41
 **/
@Data
@Configuration
public class ZkConnectConfig {

    @Value("${dap.gov.zookeeper.connectString:localhost:2181}")
    private String connectString;

    @Value("${dap.gov.zookeeper.sessionTimeoutMs:60000}")
    private int sessionTimeoutMs;

    @Value("${dap.gov.zookeeper.connectionTimeoutMs:2000}")
    private int connectionTimeoutMs;

    @Value("${dap.gov.zookeeper.sleepMsBetweenRetry:5000}")
    private int sleepMsBetweenRetry;

}
