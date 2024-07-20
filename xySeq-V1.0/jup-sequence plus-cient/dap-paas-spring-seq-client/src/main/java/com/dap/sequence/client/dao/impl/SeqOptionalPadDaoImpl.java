package com.dap.sequence.client.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.client.dao.SeqOptionalPadDao;
import com.dap.sequence.client.entity.SeqOptionalPad;
import org.springframework.stereotype.Repository;

/**
 * @className SeqOptionalPadDaoImpl
 * @description 自选补位Dao实现
 * @author renle
 * @date 2023/12/21 14:58:13 
 * @version: V23.06
 */

@Repository
public class SeqOptionalPadDaoImpl extends AbstractBaseDaoImpl<SeqOptionalPad, String> implements SeqOptionalPadDao {

    @Override
    public SeqOptionalPad getOptionalPad(SeqOptionalPad seqOptionalPad) {
        return this.getSqlSession().selectOne(getSqlNamespace().concat(".getOptionalPadOne"), seqOptionalPad);
    }

    @Override
    public Integer saveOptionalPad(SeqOptionalPad seqOptionalPad) {
        return this.getSqlSession().insert(getSqlNamespace().concat(".saveOrIgnoreOptionalPad"), seqOptionalPad);
    }

    @Override
    public void updateOptionalPad(SeqOptionalPad seqOptionalPad) {
        this.getSqlSession().update(getSqlNamespace().concat(".updateOptionalPad"), seqOptionalPad);
    }

    @Override
    public SeqOptionalPad selectForUpdatePadById(String id) {
        return this.getSqlSession().selectOne(getSqlNamespace().concat(".selectForUpdatePadById"), id);
    }
}
