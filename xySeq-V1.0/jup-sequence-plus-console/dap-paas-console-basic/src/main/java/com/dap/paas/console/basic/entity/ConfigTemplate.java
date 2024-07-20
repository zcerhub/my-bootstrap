package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @Author: GaoJun
 * @date: 2023年4月12日 17:31:13
 * @version 1.0
 * @description: 通用配置模板
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigTemplate extends BaseEntity {

    @NotBlank(message = "模板内容不能为空")
    @ApiModelProperty(value = "模板内容")
    private String content;

    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty(value = "模板名称")
    private String name;

    @NotBlank(message = "模板类型不能为空")
    @ApiModelProperty(value = "模板二级类型")
    private String moduleType;

    @ApiModelProperty(value = "模板状态 0:启用 1:禁用")
    private Integer status = 0;

    @ApiModelProperty(value = "是否删除 0:未删除 1:已删除")
    private Integer deleted = 0;

    private String updateUserName;
}
