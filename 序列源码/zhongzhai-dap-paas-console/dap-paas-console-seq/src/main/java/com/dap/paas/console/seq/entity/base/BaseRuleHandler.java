package com.dap.paas.console.seq.entity.base;

/**
 * @className BaseRuleHandler
 * @description 基础规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface BaseRuleHandler {

    /**
     * 序列生产
     *
     * @param id id
     * @param message message
     * @param ruleInfo ruleInfo
     * @return Object
     */
    Object generateRule(String id, String message, BaseRuleInfo ruleInfo);

    /**
     * 获取规则名称
     *
     * @return String
     */
    String getRuleName();
}
