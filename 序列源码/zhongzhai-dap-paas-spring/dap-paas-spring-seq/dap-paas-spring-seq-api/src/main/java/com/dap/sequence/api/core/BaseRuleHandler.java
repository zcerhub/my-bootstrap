package com.dap.sequence.api.core;


import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;

/**
 * @className BaseRuleHandler
 * @description 基础规则接口
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public interface BaseRuleHandler {

    /**
     * 	 * 根据处理器 获取一个序列号
     *
     * @param seqContext 序列设计器
     * @param rule 具体一个类型的规则 ，如数字、字符串
     * @param ruleParams ruleParams
     * @return Object
     */
    Object generateRule(SeqObj seqContext, Rule rule, RuleParams ruleParams);

    /**
     * 获取规则名称
     *
     * @return String
     */
    String getRuleName();
}
