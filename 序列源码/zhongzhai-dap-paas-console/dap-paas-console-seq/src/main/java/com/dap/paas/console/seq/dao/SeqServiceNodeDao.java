package com.dap.paas.console.seq.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqServiceNode;

import java.util.List;
import java.util.Map;

/**
 * @className SeqServiceNodeDao
 * @description 序列服务节点Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqServiceNodeDao extends BaseDao<SeqServiceNode, String> {

    /**
     * 节点查询
     *
     * @param nodeVo nodeVo
     * @return List
     */
    List<SeqServiceNodeVo> queryNodes(SeqServiceNodeVo nodeVo);

    /**
     * 更新任务
     *
     * @param instance instance
     */
    void updateJob(SeqServiceNode instance);

    /**
     * 服务监控数量
     *
     * @param map map
     * @return List
     */
    List<SeqServiceNode> serviceMonitorNumber(Map map);
}
