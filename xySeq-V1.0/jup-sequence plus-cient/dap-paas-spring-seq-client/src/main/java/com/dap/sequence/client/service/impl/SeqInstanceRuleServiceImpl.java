package com.dap.sequence.client.service.impl;


import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.client.dao.SeqInstanceRuleDao;
import com.dap.sequence.client.entity.SeqInstanceRule;
import com.dap.sequence.client.service.SeqInstanceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className SeqInstanceRuleServiceImpl
 * @description 序列实例规则实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqInstanceRuleServiceImpl extends AbstractBaseServiceImpl<SeqInstanceRule, String> implements SeqInstanceRuleService {

    @Autowired
    private SeqInstanceRuleDao seqInstanceRuleDao;

    @Override
    public List<SeqInstanceRule> selectByDesignId(String seqDesignId) {
        return seqInstanceRuleDao.selectByDesignId(seqDesignId);
    }
}
