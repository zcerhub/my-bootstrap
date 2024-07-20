package com.dap.paas.console.seq.dto;

import com.dap.paas.console.seq.entity.SeqMulticenterCluster;
import lombok.Data;

/**
 * @className SeqMulticenterClusterVo
 * @description 多中心机器
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqMulticenterClusterVo extends SeqMulticenterCluster {

    private Integer node;

    private String orgName;
}
