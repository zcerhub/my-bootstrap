package com.dap.paas.console.seq.service.impl;

import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqUseCondition;
import com.dap.paas.console.seq.feign.SeqUseConditionFeignApi;
import com.dap.paas.console.seq.service.SeqUseConditionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className SeqUseConditionServiceImpl
 * @description 序列使用情况实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqUseConditionServiceImpl implements SeqUseConditionService {

    @Autowired
    private SeqUseConditionFeignApi seqUseConditionFeignApi;

    @Override
    public ResultResponse<Page<SeqUseCondition>> queryPage(PageInDto<SeqUseCondition> param) {

        return seqUseConditionFeignApi.queryPage(param);
    }

    @Override
    public Integer saveObject(SeqUseCondition seqUseCondition) {
        ResultResponse<Integer> record = seqUseConditionFeignApi.saveObject(seqUseCondition);
        return record.getData();
    }
}
