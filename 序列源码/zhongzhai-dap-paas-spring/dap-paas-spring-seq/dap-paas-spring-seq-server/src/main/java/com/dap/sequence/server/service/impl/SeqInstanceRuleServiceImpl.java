package com.dap.sequence.server.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.server.dao.SeqInstanceRuleDao;
import com.dap.sequence.server.entity.SeqInstanceRule;
import com.dap.sequence.server.service.SeqInstanceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/1/20 9:26
 * @Descricption:
 */
@Service
public class SeqInstanceRuleServiceImpl extends AbstractBaseServiceImpl<SeqInstanceRule, String> implements SeqInstanceRuleService {

    @Autowired
    private SeqInstanceRuleDao seqInstanceRuleDao;

    @Override
    public List<SeqInstanceRule> getInstanceRuleList(Map map) {
        return seqInstanceRuleDao.getInstanceRuleList(map);
    }
}
