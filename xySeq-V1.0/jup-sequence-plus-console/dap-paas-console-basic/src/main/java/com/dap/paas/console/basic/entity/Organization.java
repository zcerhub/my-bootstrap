package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Organization extends BaseEntity {
    /**
     * 组织编码       db_column: organization_code
     */
    @ApiModelProperty(value = "组织编码")
    private String organizationCode;
    /**
     * 组织名称       db_column: organization_name
     */
    @ApiModelProperty(value = "组织名称")
    private String organizationName;

}
