package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqSdkMonitorDao;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @className SeqSdkMonitorDaoImpl
 * @description sdk监控DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqSdkMonitorDaoImpl extends AbstractBaseDaoImpl<SeqSdkMonitor, String> implements SeqSdkMonitorDao {

    @Override
    public Integer queryStatusTotal(HashMap<String, String> map) {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace().concat(".queryStatusTotal"), map);
        } catch (Exception e) {
            throw new DaoException("queryStatusTotal执行异常:" + e.getMessage());
        }
    }

    @Override
    public Integer queryNormalTotal() {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace().concat(".queryNormalTotal"));
        } catch (Exception e) {
            throw new DaoException("queryNormalTotal执行异常:" + e.getMessage());
        }
    }

    @Override
    public Integer queryExceptionTotal() {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace().concat(".queryExceptionTotal"));
        } catch (Exception e) {
            throw new DaoException("queryExceptionTotal执行异常:" + e.getMessage());
        }
    }

    @Override
    public List<SeqSdkMonitor> queryListNoPermission() {
        try {
            return this.getSqlSession().selectList(getSqlNamespace() + ".queryListNoPermission");
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":queryListNoPermission " + e.getMessage();
            throw new DaoException(msg);
        }
    }

    @Override
    public Integer updateNoPermission(SeqSdkMonitor seqSdkMonitor) {
        try {
            return this.getSqlSession().update(getSqlNamespace() + ".updateNoPermission", seqSdkMonitor);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.UpdateObject.getName() + ":updateNoPermission " + e.getMessage();
            throw new DaoException(msg);
        }
    }

    /**
     * 更新全部信息
     *
     * @param seqSdkMonitor
     * @return
     */
    @Override
    public int updateAllInfoWithVersion(SeqSdkMonitor seqSdkMonitor) {
        try {
            return this.getSqlSession().update(getSqlNamespace().concat(".updateAllInfoWithVersion"), seqSdkMonitor);
        } catch (Exception e) {
            throw new DaoException("updateAllInfoWithVersion执行异常:" + e.getMessage());
        }
    }
}
