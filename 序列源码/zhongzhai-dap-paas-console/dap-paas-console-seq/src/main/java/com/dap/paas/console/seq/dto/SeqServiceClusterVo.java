package com.dap.paas.console.seq.dto;

import com.dap.paas.console.seq.entity.SeqServiceCluster;
import lombok.Data;

/**
 * @className SeqServiceClusterVo
 * @description 服务集群
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqServiceClusterVo extends SeqServiceCluster {

    private String machineName;

    private String unitName;

    private String machineRoomCode;

    private String cityName;

    private String orgName;

    private Integer node;

    private Integer healthy;

    private Integer abnormal;

    private String currentDb;

    private String currentDbName;

    private String seqClusterId;

    private Integer syncMethod;
}
