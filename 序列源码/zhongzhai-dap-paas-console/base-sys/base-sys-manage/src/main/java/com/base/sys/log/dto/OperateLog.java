package com.base.sys.log.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author qqqab
 */
@Data
@Builder
public class OperateLog {
    private String operId;//	操作序号	TRUE
    private String title;//	操作模块	TRUE
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
}
