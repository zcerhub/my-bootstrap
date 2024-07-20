package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqServiceNodeDao;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @className SeqServiceNodeDaoImpl
 * @description 序列服务节点DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqServiceNodeDaoImpl extends AbstractBaseDaoImpl<SeqServiceNode, String> implements SeqServiceNodeDao {

    @Override
    public List<SeqServiceNodeVo> queryNodes(SeqServiceNodeVo node) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace().concat(".queryNodes"), node);
        } catch (Exception e) {
            throw new DaoException("queryNodes执行异常:" + e.getMessage());
        }
    }

    @Override
    public void updateJob(SeqServiceNode node) {
        try {
            this.getSqlSession().update(getSqlNamespace().concat(".updateJob"), node);
        } catch (Exception e) {
            throw new DaoException("updateJob执行异常:" + e.getMessage());
        }
    }

    @Override
    public List<SeqServiceNode> serviceMonitorNumber(Map map) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace().concat(".serviceMonitorNumber"), map);
        } catch (Exception e) {
            throw new DaoException("查询集群下实例不同状态的实例数量:" + e.getMessage());
        }
    }
}
