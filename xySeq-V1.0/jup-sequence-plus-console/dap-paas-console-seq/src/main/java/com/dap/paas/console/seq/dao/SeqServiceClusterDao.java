package com.dap.paas.console.seq.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.seq.dto.SeqServiceClusterVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;

import java.util.List;

/**
 * @className SeqServiceClusterDao
 * @description 序列服务集群Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqServiceClusterDao extends BaseDao<SeqServiceCluster, String> {

    /**
     * 获取任务列表
     *
     * @param registerCluster registerCluster
     * @return List
     */
    List<SeqServiceCluster> queryListJob(SeqServiceCluster registerCluster);

    /**
     * 更新任务
     *
     * @param cluster cluster
     */
    void updateJob(SeqServiceCluster cluster);

    /**
     * 获取服务集群信息
     *
     * @param cluster cluster
     * @return SeqServiceClusterVo
     */
    SeqServiceClusterVo getClusterInfo(SeqServiceCluster cluster);
}
