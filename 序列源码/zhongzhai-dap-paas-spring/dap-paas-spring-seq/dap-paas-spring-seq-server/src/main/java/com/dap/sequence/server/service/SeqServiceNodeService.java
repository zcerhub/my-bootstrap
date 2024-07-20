package com.dap.sequence.server.service;


import com.base.core.service.BaseService;
import com.dap.sequence.server.entity.SeqServiceNode;

/**
 * @author scalor
 * @date 2022/1/18
 */
public interface SeqServiceNodeService extends BaseService<SeqServiceNode, String> {

    /**
     * 获取集群名称
     *
     * @param seqName seqName
     * @return SeqServiceNode
     */
    SeqServiceNode getObjectByClusterName(String seqName);
}
