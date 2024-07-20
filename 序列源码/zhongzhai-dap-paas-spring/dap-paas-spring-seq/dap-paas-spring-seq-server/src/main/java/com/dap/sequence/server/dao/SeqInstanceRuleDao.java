package com.dap.sequence.server.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.dap.sequence.server.entity.SeqInstanceRule;

import java.util.List;
import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/1/20 9:19
 * @Descricption:
 */
public interface SeqInstanceRuleDao extends BaseDao<SeqInstanceRule, String> {

    /**
     * 获取序列规则信息
     *
     * @param map map
     * @return List
     * @throws DaoException DaoException
     */
    List<SeqInstanceRule> getInstanceRuleList(Map map);
}
