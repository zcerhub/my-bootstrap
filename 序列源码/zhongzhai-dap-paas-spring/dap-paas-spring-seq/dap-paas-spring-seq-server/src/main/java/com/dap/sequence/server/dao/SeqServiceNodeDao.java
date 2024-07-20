package com.dap.sequence.server.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.server.entity.SeqServiceNode;

/**
 * @author scalor
 * @date 2022/1/18
 */
public interface SeqServiceNodeDao extends BaseDao<SeqServiceNode, String> {

    /**
     * 获取集群信息
     *
     * @param seqName seqName
     * @return SeqServiceNode
     */
    SeqServiceNode getObjectByClusterName(String seqName);
}
