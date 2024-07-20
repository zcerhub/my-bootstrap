package com.dap.paas.console.seq.service;

import com.base.api.exception.ServiceException;
import com.base.core.service.BaseService;
import com.dap.paas.console.seq.entity.SeqInstanceRule;

import java.util.List;
import java.util.Map;

/**
 * @className SeqInstanceRuleService
 * @description 序列规则实例接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqInstanceRuleService extends BaseService<SeqInstanceRule, String> {

    /**
     * 获取实例列表
     *
     * @param map map
     * @return List
     */
    List<SeqInstanceRule> getInstanceRuleList(Map map);

    /**
     * 删除实例规则
     *
     * @param seqDesignId seqDesignId
     * @return Integer
     */
    Integer delObjectByDesignId(String seqDesignId);
}
