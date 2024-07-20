package com.dap.sequence.client.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.client.entity.SeqInstanceRule;

import java.util.List;

/**
 * @className SeqInstanceRuleDao
 * @description 序列实例规则Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqInstanceRuleDao extends BaseDao<SeqInstanceRule, String> {


    /**
     * 通过design_id序列设计id查询数
     *
     * @param seqDesignId seqDesignId
     * @return List
     */
    List<SeqInstanceRule> selectByDesignId(String seqDesignId);

}
