package com.dap.paas.console.seq.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.seq.dao.SeqTemplateRuleDao;
import com.dap.paas.console.seq.entity.SeqTemplateRule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @className SeqTemplateRuleDaoImpl
 * @description 序列模板规则DaoImpl
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Repository
public class SeqTemplateRuleDaoImpl extends AbstractBaseDaoImpl<SeqTemplateRule, String> implements SeqTemplateRuleDao {

    @Override
    public List<SeqTemplateRule> queryListByMap(Map map) {
        try {
            return this.getSqlSession().selectList(getSqlNamespace() + ".queryListByMap", map);
        } catch (Exception e) {
            throw new DaoException("queryListByMap执行异常" + e.getMessage());
        }
    }
}
