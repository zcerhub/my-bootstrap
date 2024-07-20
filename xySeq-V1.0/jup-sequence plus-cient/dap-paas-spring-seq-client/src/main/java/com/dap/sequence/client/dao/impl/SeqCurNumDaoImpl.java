package com.dap.sequence.client.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.client.dao.SeqCurNumDao;
import com.dap.sequence.client.entity.SeqCurNum;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/2/7 13:53
 * @Descricption:
 */
@Repository
public class SeqCurNumDaoImpl extends AbstractBaseDaoImpl<SeqCurNum, String> implements SeqCurNumDao {

    @Override
    public SeqCurNum getObjectByCode(Map<String, String> map) {
        return this.getSqlSession().selectOne(getSqlNamespace() + ".getObjectByCode", map);
    }

    @Override
    public SeqCurNum selectForUpdateById(String id) {
        return this.getSqlSession().selectOne(getSqlNamespace() + ".selectForUpdateById", id);
    }

    @Override
    public SeqCurNum selectForUpdateBySeq(SeqCurNum query) {
        return this.getSqlSession().selectOne(getSqlNamespace() + ".selectForUpdateBySeq", query);
    }

    public void deleteRule(String ruleId, String dateBefore) {
        SeqCurNum seqCurNum = new SeqCurNum();
        seqCurNum.setSeqInstanceRuleId(ruleId);
        seqCurNum.setInDay(dateBefore);
        this.getSqlSession().delete(getSqlNamespace() + ".deleteRule", seqCurNum);
    }
}