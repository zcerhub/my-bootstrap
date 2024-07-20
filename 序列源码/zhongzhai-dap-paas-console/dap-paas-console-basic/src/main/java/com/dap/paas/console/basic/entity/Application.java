package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Application extends BaseEntity {
    /**
     * 应用编码       db_column: application_code
     */
    @ApiModelProperty(value = "应用编码")
    private String applicationCode;
    /**
     * 应用名称       db_column: application_name
     */
    @ApiModelProperty(value = "应用名称")
    private String applicationName;
    /**
     * 组织编码       db_column: organization_id
     */
    @ApiModelProperty(value = "组织编码")
    private String organizationId;
    /**
     * 负责人       db_column: charge_user
     */
    @ApiModelProperty(value = "负责人")
    private String chargeUser;
    /**
     * 描述       db_column: description
     */
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "接口访问accessKey")
    private String accessKey;

    @ApiModelProperty(value = "接口访问accessKey启用状态 ‘1’-启用，‘0’-禁用")
    private String accessKeyStatus;

}
