package com.dap.paas.console.seq.service;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqRuleUpdateOrder;

import java.util.List;
import java.util.Map;

/**
 * @className SeqInstanceRuleService
 * @description 序列规则实例接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqInstanceRuleService {

    /**
     * 序列设计规则列表
     *
     * @param param param
     * @return Result
     */
    ResultResponse<Page<SeqInstanceRule>> queryPage(PageInDto<SeqInstanceRule> param);

    /**
     * 序列设计规则新增
     *
     * @param seqInstanceRule seqInstanceRule
     * @return Result
     */
    ResultResponse<ExceptionEnum> insert(SeqInstanceRule seqInstanceRule);

    /**
     * 序列设计规则编辑
     *
     * @param seqInstanceRule seqInstanceRule
     * @return Result
     */
    ResultResponse<ExceptionEnum> update(SeqInstanceRule seqInstanceRule);
    /**
     * 规则修改
     *
     * @param seqRuleUpdateOrder seqRuleUpdateOrder
     * @return Result
     */
    ResultResponse<ExceptionEnum> updateOrder(SeqRuleUpdateOrder seqRuleUpdateOrder);

    /**
     * 序列设计规则删除
     *
     * @param id id
     * @return Result
     */
    ResultResponse<ExceptionEnum> delete(String id);

    /**
     * 序列测试
     *
     * @param seqNo seqNo
     * @return Result
     */
    ResultResponse<String> testSeqCode(String seqNo);


    /**
     * 获取实例列表
     *
     * @param map map
     * @return List
     */
    ResultResponse<List<SeqInstanceRule>> queryList(Map map);

    /**
     * 删除实例规则
     *
     * @param seqDesignId seqDesignId
     * @return Integer
     */
    ResultResponse<Integer> delObjectByDesignId(String seqDesignId);


}
