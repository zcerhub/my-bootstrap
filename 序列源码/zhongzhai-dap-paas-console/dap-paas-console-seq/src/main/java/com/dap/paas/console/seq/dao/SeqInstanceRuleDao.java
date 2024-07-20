package com.dap.paas.console.seq.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.seq.entity.SeqInstanceRule;

import java.util.List;
import java.util.Map;

/**
 * @className SeqInstanceRuleDao
 * @description 序列实例规则Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqInstanceRuleDao extends BaseDao<SeqInstanceRule, String> {

    /**
     * 获取实例规则
     *
     * @param map map
     * @return List
     */
    List<SeqInstanceRule> getInstanceRuleList(Map map);

    /**
     * 通过id删除规则
     *
     * @param seqDesignId seqDesignId
     * @return Integer
     */
    Integer delObjectByDesignId(String seqDesignId);

}
