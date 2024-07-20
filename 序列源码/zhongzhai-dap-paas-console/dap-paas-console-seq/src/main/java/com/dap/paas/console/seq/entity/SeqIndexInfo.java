package com.dap.paas.console.seq.entity;


import com.dap.paas.console.seq.dto.SeqServiceStatusDTO;
import lombok.Data;

import java.util.List;

/**
 * @className SeqIndexInfo
 * @description 序列索引信息
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqIndexInfo {

    /**
     * 版本
     */
    private String seqVersion = "V1.0";

    /**
     * 系统数量
     */
    private String osTotal;

    /**
     * 模板数量
     */
    private String tempTotal;

    /**
     * 规则数量
     */
    private String rulesTotal;

    /**
     * 模块数量
     */
    private String moduleTotal;

    /**
     * SDK状态 正常
     */
    private String sdkNormalTotal;

    /**
     * SDK状态 异常
     */
    private String sdkExceptionTotal;

    /**
     * SDK 列表
     */
    private List<SeqSdkMonitor> SeqSdkMonitorList;

    /**
     * 服务状态
     */
    private SeqServiceStatusDTO seqServiceStatusDTO;

    /**
     * 服务列表
     */
    private List<SeqServiceNode> seqServiceNodeList;

}
