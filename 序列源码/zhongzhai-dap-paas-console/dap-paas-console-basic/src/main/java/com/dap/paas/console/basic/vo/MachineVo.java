package com.dap.paas.console.basic.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MachineVo {
    /**
     * 主机id
     */
    private String id;
    /**
     * 主机IP
     */
    private String hostIp;
    /**
     * 主机端口
     */
    private Integer hostPort;
    /**
     * 主机备注
     */
    private String hostRemark;

    /**
     * ssh账号
     */
    private String hostSshAccount;
    /**
     * 所属组织
     */
    private String organization;
    /**
     * 所属城市
     */
    private String city;
    /**
     * 所属机房
     */
    private String computerRoom;
    /**
     * 操作系统类型，如redhat,ubuntu
     */
    private String osRelease;
    /**
     * 操作系统版本，如7，6
     */
    private String osVersion;
    /**
     * 机器是否可用0；可用，1：不可用
     */
    private String available;
    /**
     * 机器内存
     */
    private String memory;
    /**
     * 机器cpu
     */
    private String cpu;
    /**
     * 机器磁盘
     */
    private String disk;
    /**
     * CPU内核
     */
    private String coreArch;

}
