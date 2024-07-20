package com.dap.sequence.server.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.api.dto.SnowflakeRcvCluster;
import com.dap.sequence.server.entity.SeqSdkMonitor;

import java.util.List;
import java.util.Map;

/**
 * @author: liu
 * @date: 2022/2/24 10:46
 * @description:
 */
public interface SeqSdkMonitorDao extends BaseDao<SeqSdkMonitor, String> {

    /**
     * 更新全部信息
     *
     * @param seqSdkMonitor
     * @return
     */
    int updateAllInfoWithVersion(SeqSdkMonitor seqSdkMonitor);

    /**
     * 查询可以回收的进群信息
     *
     * @param paramMap
     * @return
     */
    List<SnowflakeRcvCluster> queryClusterForRcv(Map<String, Object> paramMap);


    /**
     * 查询最大的workId
     *
     * @param paramMap
     * @return
     */
    Integer queryMaxWorkId(Map<String, Object> paramMap);

    /**
     * 回收workId
     *
     * @param paramMap
     * @return
     */
    int updateStatus(Map<String, Object> paramMap);

    /**
     * 通过hostIp和port查看所有的非MONITOR_STATUS_DISCARDED的SeqSdkMonitor
     *
     * @param paramMap
     * @return
     */
    List<SeqSdkMonitor> querySeqSdkMonitorByHostIpAndPort(Map<String, Object> paramMap);


}
