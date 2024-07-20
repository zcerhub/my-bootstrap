package com.dap.paas.console.seq.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.seq.dao.SeqInstanceRuleDao;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<SeqInstanceRule> getInstanceRuleList(Map map) {
        return seqInstanceRuleDao.getInstanceRuleList(map);
    }

    @Override
    public Integer delObjectByDesignId(String seqDesignId) {
        return seqInstanceRuleDao.delObjectByDesignId(seqDesignId);
    }
}
