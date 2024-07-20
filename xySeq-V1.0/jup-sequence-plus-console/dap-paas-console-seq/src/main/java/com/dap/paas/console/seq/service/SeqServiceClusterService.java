package com.dap.paas.console.seq.service;

import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.dap.paas.console.seq.dto.SeqServiceClusterVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;

/**
 * @className SeqServiceClusterService
 * @description 序列服务集群接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqServiceClusterService extends BaseService<SeqServiceCluster, String> {

    /**
     * 删除集群
     *
     * @param id id
     * @return Result
     */
    Result delete(String id);

    /**
     * 更新集群
     *
     * @param centervCluster centervCluster
     * @return Result
     */
    Result update(SeqServiceCluster centervCluster);

    /**
     * 新增集群
     *
     * @param centervCluster centervCluster
     * @return Result
     */
    Result insert(SeqServiceCluster centervCluster);

    /**
     * 集群启动
     *
     * @param cluster cluster
     * @return Result
     */
    Result start(SeqServiceCluster cluster);

    /**
     * 集群停止
     *
     * @param cluster cluster
     * @return Result
     */
    Result stop(SeqServiceCluster cluster);

    /**
     * 获取集群信息
     *
     * @param cluster cluster
     * @return SeqServiceClusterVo
     */
    SeqServiceClusterVo getClusterInfo(SeqServiceCluster cluster);
}
