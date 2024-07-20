package com.dap.paas.console.seq.service;

import com.base.core.service.BaseService;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;

import java.util.List;

/**
 * @className SeqSdkMonitorService
 * @description 序列SDK监控接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqSdkMonitorService extends BaseService<SeqSdkMonitor, String> {

    /**
     * 获取SDK监控正常数量
     *
     * @return Integer
     */
    Integer queryNormalTotal();

    /**
     * 获取SDK监控异常数量
     *
     * @return Integer
     */
    Integer queryExceptionTotal();

    /**
     * 获取SDK监控列表
     *
     * @return List
     */
    List<SeqSdkMonitor> queryListNoPermission();

    /**
     * 更新SDK监控
     *
     * @param seqSdkMonitor seqSdkMonitor
     * @return Integer
     */
    Integer updateNoPermission(SeqSdkMonitor seqSdkMonitor);

    /**
     * 手动回收接口
     *
     * @param paramWorkData
     * @return
     */
    boolean rcvWorIdByClient(SeqSdkMonitor paramWorkData);

}
