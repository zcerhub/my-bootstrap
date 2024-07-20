package com.dap.paas.console.basic.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

@Data
public class DeployClient extends BaseEntity {

    /**
     * 实例名称       db_column: name
     */
    private String name;
    /**
     * 主机地址       db_column: host_address
     */
    private String hostAddress;
    /**
     * 主机端口      db_column: host_port
     */
    private String hostPort;
    /**
     * 注解信息      db_column: remark
     */
    private String remark;
    /**
     * 版本      db_column: pack_version
     */
    private String packVersion;
    /**
     * 状态      db_column: state
     */
    private int state;
}
