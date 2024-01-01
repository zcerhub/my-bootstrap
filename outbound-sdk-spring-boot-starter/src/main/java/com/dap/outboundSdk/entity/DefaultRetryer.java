package com.dap.outboundSdk.entity;

import lombok.Data;

/**
 * @description: 封装default-retryer属性类
 * @author: zhangce
 * @create: 2024/1/1 10:46
 **/
@Data
public class DefaultRetryer {

    private boolean enable;
    private long period;
    private long maxPeriod;
    private int maxAttempts;

}
