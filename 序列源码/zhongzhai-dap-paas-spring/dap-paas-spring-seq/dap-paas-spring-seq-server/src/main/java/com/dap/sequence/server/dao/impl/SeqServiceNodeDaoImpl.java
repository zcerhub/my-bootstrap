package com.dap.sequence.server.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.server.dao.SeqServiceNodeDao;
import com.dap.sequence.server.entity.SeqServiceNode;
import org.springframework.stereotype.Repository;

/**
 * @author scalor
 * @date 2022/1/18
 */
@Repository
public class SeqServiceNodeDaoImpl extends AbstractBaseDaoImpl<SeqServiceNode, String> implements SeqServiceNodeDao {
    @Override
    public SeqServiceNode getObjectByClusterName(String seqName) {
        return this.getSqlSession().selectOne(getSqlNamespace() + ".getObjectByClusterName", seqName);
    }
}
