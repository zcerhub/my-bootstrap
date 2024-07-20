package com.dap.paas.console.seq.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;

import java.util.HashMap;
import java.util.List;

/**
 * @className SeqSdkMonitorDao
 * @description sdk监控dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqSdkMonitorDao extends BaseDao<SeqSdkMonitor, String> {

    /**
     * 获取状态总数
     *
     * @param map map
     * @return Integer
     */
    Integer queryStatusTotal(HashMap<String, String> map);

    /**
     * 获取正常总数
     *
     * @return Integer
     */
    Integer queryNormalTotal();

    /**
     * 获取异常总数
     *
     * @return Integer
     */
    Integer queryExceptionTotal();

    /**
     * 获取没有权限列表
     *
     * @return List
     */
    List<SeqSdkMonitor> queryListNoPermission();

    /**
     * 更新没有权限
     *
     * @param seqSdkMonitor seqSdkMonitor
     * @return Integer
     */
    Integer updateNoPermission(SeqSdkMonitor seqSdkMonitor);


    /**
     * 更新全部信息
     *
     * @param seqSdkMonitor
     * @return
     */
    int updateAllInfoWithVersion(SeqSdkMonitor seqSdkMonitor);

}
