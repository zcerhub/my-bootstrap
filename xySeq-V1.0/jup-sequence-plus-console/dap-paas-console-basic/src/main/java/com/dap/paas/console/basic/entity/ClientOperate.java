package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.basic.enums.ClientEnv;
import io.swagger.annotations.ApiModelProperty;

public class ClientOperate extends BaseEntity {

    @ApiModelProperty(value = "实例ID")
    private String clientId;
    @ApiModelProperty(value = "操作类型")
    private String type;
    @ApiModelProperty(value = "日志ID")
    private String logId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
