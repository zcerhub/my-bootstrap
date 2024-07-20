package com.dap.paas.console.basic.config.executor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "executor")
@Data
public class ExecutorProperties {
    private int corePoolSize = 20;
    private int keepAliveSeconds = 60;
    private int maxPoolSize = 100;
    private int queueCapacity = 100;
    private boolean allowCoreThreadTimeOut = false;
}
