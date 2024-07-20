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
    //标识是否将本 操作审计 已经进行同步 1 标识同步成功 0 标识同步失败
    private Integer asyncStatus;

    //菜单
    private String menuName;

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

    public Integer getAsyncStatus() {
        return asyncStatus;
    }

    public void setAsyncStatus(Integer asyncStatus) {
        this.asyncStatus = asyncStatus;
    }
}
