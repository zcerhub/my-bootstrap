package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqDesignDao;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @className SeqDesignDaoImpl
 * @description 序列设计DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqDesignDaoImpl extends AbstractBaseDaoImpl<SeqDesignPo, String> implements SeqDesignDao {

    @Override
    public SeqDesignPo getObjectByCode(Map map) {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace() + ".getObjectByCode", map);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.GetObjectById.getName() + ":getObjectByCode " + e.getMessage();
            throw new DaoException(msg);
        }
    }

    @Override
    public int queryApplicationTotal() {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace().concat(".queryApplicationTotal"));
        } catch (Exception e) {
            throw new DaoException("queryApplicationTotal执行异常:" + e.getMessage());
        }
    }

    @Override
    public Integer updateStatusByIds(Map<String, Object> paramMap) {
        try {
            return this.getSqlSession().update(getSqlNamespace() + ".updateStatusByIds", paramMap);
        } catch (Exception e) {
            throw new DaoException("updateStatusByIds 执行异常" + e.getMessage());
        }
    }

    @Override
    public Integer updateObjectBatch(List<SeqDesignPo> seqDesignList) {
        try {
            return this.getSqlSession().update(getSqlNamespace() + ".updateObjectBatch", seqDesignList);
        } catch (Exception e) {
            throw new DaoException("updateObjectBatch 执行异常" + e.getMessage());
        }
    }
}
