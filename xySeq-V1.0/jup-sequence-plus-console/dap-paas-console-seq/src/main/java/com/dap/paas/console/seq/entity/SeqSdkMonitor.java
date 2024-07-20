package com.dap.paas.console.seq.entity;


import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.constant.SeqConstant;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * @className SeqSdkMonitor
 * @description 序列SDK监控实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqSdkMonitor extends BaseEntity {

    /**
     * 应用名称
     */
    private String serviceName;

    /**
     * sdk服务ip
     */
    private String hostIp;

    /**
     * sdk服务端口
     */
    private String port;

    /**
     * sdk服务状态
     */
    private String status;

    /**
     * 应用唯一标识
     */
    private String serviceId;

    private String sdkName;

    private String instanceName;

    public String getStatus() {
        if (StringUtils.isNotBlank(status)) {
            return status;
        }
        if (getUpdateDate() == null ||
                new Date().getTime() - getUpdateDate().getTime() > 60 * 1000) {
            return SeqConstant.SEQ_NODE_STATUS_STOP;
        }
        return SeqConstant.SEQ_NODE_STATUS_RUN;
    }

    private String contextPath;
}
