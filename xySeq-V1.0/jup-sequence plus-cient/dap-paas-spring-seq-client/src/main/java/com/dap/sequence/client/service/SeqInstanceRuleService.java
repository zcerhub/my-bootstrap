package com.dap.sequence.client.service;

import com.base.core.service.BaseService;
import com.dap.sequence.client.entity.SeqInstanceRule;

import java.util.List;

/**
 * @className SeqInstanceRuleService
 * @description 序列规则实例接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqInstanceRuleService extends BaseService<SeqInstanceRule, String> {


    List<SeqInstanceRule> selectByDesignId(String seqDesignId);
    /**
     * 根据id查询编排信息
     *
     * @param String code
     * @return SeqDesignPo
     */
}
