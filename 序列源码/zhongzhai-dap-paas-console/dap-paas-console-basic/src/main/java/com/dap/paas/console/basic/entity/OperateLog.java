package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author qqqab
 */
@Data
public class OperateLog extends BaseEntity {
    @NotNull(message = "操作序号不能为空")
    private String operId;//	操作序号	TRUE
    @NotNull(message = "操作模块不能为空")
    private String title;//	操作模块	TRUE
    @NotNull(message = "租户编码不能为空")
    private String tenantCode;//	租户编码	FALSE
    private String componentId;//	组件Id	FALSE
    private String businessType;//	业务类型	FALSE
    private String method;//	请求方法	FALSE
    private String requestMethod;//	请求方式	FALSE
    private Integer operatorType;//	操作类别	FALSE
    private String operName;//	操作人员	FALSE
    private String operUrl;//	请求地址	FALSE
    private String operIp;//	操作地址	FALSE
    private String operLocation;//	操作地点	FALSE
    private Integer status;//0表示同步成功 1表示同步失败
    private Date updateDate;
    private Date createDate;
}
