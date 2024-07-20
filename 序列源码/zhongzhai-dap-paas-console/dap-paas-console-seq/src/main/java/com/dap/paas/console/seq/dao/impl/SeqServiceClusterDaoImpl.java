package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqServiceClusterDao;
import com.dap.paas.console.seq.dto.SeqServiceClusterVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @className SeqServiceClusterDaoImpl
 * @description 序列服务集群DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqServiceClusterDaoImpl extends AbstractBaseDaoImpl<SeqServiceCluster, String> implements SeqServiceClusterDao {

    @Override
    public List<SeqServiceCluster> queryListJob(SeqServiceCluster registerCluster) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace().concat(".queryListJob"), registerCluster);
        } catch (Exception e) {
            throw new DaoException("queryListJob执行异常:" + e.getMessage());
        }
    }

    @Override
    public void updateJob(SeqServiceCluster cluster) {
        try {
            this.getSqlSession().update(getSqlNamespace().concat(".updateJob"), cluster);
        } catch (Exception e) {
            throw new DaoException("updateJob执行异常:" + e.getMessage());
        }
    }

    @Override
    public SeqServiceClusterVo getClusterInfo(SeqServiceCluster cluster) {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace().concat(".getClusterInfo"), cluster);
        } catch (Exception e) {
            throw new DaoException("getClusterInfo 执行异常:" + e.getMessage());
        }
    }
}
