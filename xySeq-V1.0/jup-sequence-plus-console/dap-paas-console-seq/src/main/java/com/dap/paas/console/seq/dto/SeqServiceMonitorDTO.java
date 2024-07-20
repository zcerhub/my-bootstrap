package com.dap.paas.console.seq.dto;

import com.dap.paas.console.seq.entity.SeqServiceNode;
import lombok.Data;

import java.util.List;

/**
 * @className SeqServiceMonitorDTO
 * @description 服务监控
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqServiceMonitorDTO extends SeqServiceStatusDTO {

    /**
     * 集群下所有实例集合
     */
    private List<SeqServiceNode> seqServiceNodeList;

}
