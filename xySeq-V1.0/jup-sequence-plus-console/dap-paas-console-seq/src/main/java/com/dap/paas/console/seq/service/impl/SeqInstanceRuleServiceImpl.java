package com.dap.paas.console.seq.service.impl;

import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqRuleUpdateOrder;
import com.dap.paas.console.seq.feign.SeqInstanceRuleFeignApi;
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
public class SeqInstanceRuleServiceImpl implements SeqInstanceRuleService {

    @Autowired
    private SeqInstanceRuleFeignApi seqInstanceRuleFeignApi;

    @Override
    public ResultResponse<List<SeqInstanceRule>> queryList(Map map) {
        return seqInstanceRuleFeignApi.queryList(map);
    }

    @Override
    public ResultResponse<Integer> delObjectByDesignId(String seqDesignId) {
        return seqInstanceRuleFeignApi.delObjectByDesignId(seqDesignId);
    }

    @Override
    public ResultResponse<Page<SeqInstanceRule>> queryPage(PageInDto<SeqInstanceRule> param) {

        return seqInstanceRuleFeignApi.queryPage(param);
    }

    @Override
    public ResultResponse<ExceptionEnum> insert(SeqInstanceRule seqInstanceRule) {

        return seqInstanceRuleFeignApi.insert(seqInstanceRule);
    }
    @Override
    public ResultResponse<ExceptionEnum> update(SeqInstanceRule seqInstanceRule) {

        return seqInstanceRuleFeignApi.update(seqInstanceRule);
    }

    @Override
    public ResultResponse<ExceptionEnum> updateOrder(SeqRuleUpdateOrder seqRuleUpdateOrder) {

        return seqInstanceRuleFeignApi.updateOrder(seqRuleUpdateOrder);
    }

    @Override
    public ResultResponse<ExceptionEnum> delete(String id) {

        return seqInstanceRuleFeignApi.delete(id);
    }

    @Override
    public ResultResponse<String> testSeqCode(String seqNo) {

        return seqInstanceRuleFeignApi.testSeqCode(seqNo);
    }
}
