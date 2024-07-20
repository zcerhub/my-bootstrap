package com.dap.paas.console.seq.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;

import java.util.List;

/**
 * @className SeqMulticenterNodeDao
 * @description 多中心节点Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqMulticenterNodeDao extends BaseDao<SeqMulticenterNode, String> {

    /**
     * 查询节点信息
     *
     * @param node node
     * @return List
     */
    List<SeqMulticenterNode> queryNodes(SeqMulticenterNode node);
}
