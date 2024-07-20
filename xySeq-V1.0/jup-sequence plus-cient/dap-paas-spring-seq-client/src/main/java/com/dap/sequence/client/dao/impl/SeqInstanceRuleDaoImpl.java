package com.dap.sequence.client.dao.impl;



import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.sequence.client.dao.SeqInstanceRuleDao;
import com.dap.sequence.client.entity.SeqInstanceRule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @className SeqInstanceRuleDaoImpl
 * @description 序列实例规则DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqInstanceRuleDaoImpl extends AbstractBaseDaoImpl<SeqInstanceRule, String> implements SeqInstanceRuleDao {

    @Override
    public List<SeqInstanceRule> selectByDesignId(String seqDesignId) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace() + ".selectByDesignId", seqDesignId);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":selectByDesignId " + e.getMessage();
            throw new DaoException(msg);
        }
    }
}
