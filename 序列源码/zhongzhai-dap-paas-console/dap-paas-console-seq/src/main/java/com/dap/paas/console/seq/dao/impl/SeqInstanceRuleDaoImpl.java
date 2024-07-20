package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqInstanceRuleDao;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @className SeqInstanceRuleDaoImpl
 * @description 序列实例规则DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqInstanceRuleDaoImpl extends AbstractBaseDaoImpl<SeqInstanceRule, String> implements SeqInstanceRuleDao {

    @Override
    public List<SeqInstanceRule> getInstanceRuleList(Map map) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace() + ".InstanceRuleList", map);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":InstanceRuleList " + e.getMessage();
            throw new DaoException(msg);
        }
    }

    @Override
    public Integer delObjectByDesignId(String seqDesignId) {
        try {
            return this.getSqlSession().update(getSqlNamespace() + ".delObjectByDesignId", seqDesignId);
        } catch (Exception e) {
            throw new DaoException("delObjectByDesignId执行异常" + e.getMessage());
        }
    }
}
