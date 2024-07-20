package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

public class ShellLog extends BaseEntity {
    /**
     * 状态       db_column: status
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 内容       db_column: content
     */
    @ApiModelProperty(value = "内容")
    private String content;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
