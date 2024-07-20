package com.dap.paas.console.seq.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.seq.entity.SeqTemplateRule;

import java.util.List;
import java.util.Map;

/**
 * @className SeqTemplateRuleDao
 * @description 序列模板规则Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqTemplateRuleDao extends BaseDao<SeqTemplateRule, String> {

    /**
     * 获取模板规则
     *
     * @param map map
     * @return List
     */
    List<SeqTemplateRule> queryListByMap(Map map);

}
