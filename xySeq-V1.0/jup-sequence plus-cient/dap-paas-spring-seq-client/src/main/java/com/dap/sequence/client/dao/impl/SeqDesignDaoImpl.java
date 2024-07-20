package com.dap.sequence.client.dao.impl;



import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.client.dao.SeqDesignDao;
import com.dap.sequence.client.entity.SeqDesignPo;
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
    public SeqDesignPo getObjectByid(String id) {
        try {
            return this.getSqlSession().selectOne(getSqlNamespace() + ".selectObjectById", id);
        } catch (Exception e) {
            throw new DaoException("selectObjectById执行异常:" + e.getMessage());
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

    @Override
    public List<SeqDesignPo> selectCode() {
        try {
            return this.getSqlSession().selectList(getSqlNamespace() + ".selectCode");
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":selectCode " + e.getMessage();
            throw new DaoException(msg);
        }
    }
    @Override
    public SeqDesignPo getLocalObjectByCode(String code) {

        try {
            return this.getSqlSession().selectOne(getSqlNamespace() + ".getLocalObjectByCode", code);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.GetObjectById.getName() + ":getLocalObjectByCode " + e.getMessage();
            throw new DaoException(msg);
        }
    }
}
