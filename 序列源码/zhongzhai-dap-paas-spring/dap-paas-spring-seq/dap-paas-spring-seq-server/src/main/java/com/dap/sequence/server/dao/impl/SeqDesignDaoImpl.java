package com.dap.sequence.server.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.server.dao.SeqDesignDao;
import com.dap.sequence.server.entity.SeqDesignPo;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/1/19 14:00
 * @Descricption:
 */
@Repository
public class SeqDesignDaoImpl extends AbstractBaseDaoImpl<SeqDesignPo, String> implements SeqDesignDao {
    @Override
    public SeqDesignPo getObjectByCode(Map<String, String> map) {
        return this.getSqlSession().selectOne(getSqlNamespace() + ".getObjectByCode", map);
    }

    @Override
    public int checkAccessKey(Map<String, String> map) {
        return this.getSqlSession().selectOne(getSqlNamespace() + ".checkAccessKey", map);
    }
}
