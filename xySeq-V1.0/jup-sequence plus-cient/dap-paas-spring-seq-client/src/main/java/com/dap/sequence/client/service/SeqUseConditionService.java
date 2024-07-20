package com.dap.sequence.client.service;

import com.base.core.service.BaseService;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.entity.SeqUseCondition;

import java.util.List;

/**
 * @author: liu
 * @date: 2022/2/22
 * @description:
 */
public interface SeqUseConditionService extends BaseService<SeqUseCondition, String> {

    /**
     * 存储序列生成信息
     *
     *@param   obj 生成序列集合
     *@param   sequenceCode 序列编号
     *@param   seqDesignPo seqDesignPo
     *@param   clientInfo clientInfo
     */
    void saveUseCondition(List<Object> obj, String sequenceCode, SeqDesignPo seqDesignPo, String clientInfo);
}
