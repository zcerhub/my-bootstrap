package com.dap.paas.console.basic.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ClientOperateVo {
    private String id;
    private String clientName;
    private String type;
    private String logStatus;
    private String logContent;
    private String operateUser;
    private Date operateTime;
}
