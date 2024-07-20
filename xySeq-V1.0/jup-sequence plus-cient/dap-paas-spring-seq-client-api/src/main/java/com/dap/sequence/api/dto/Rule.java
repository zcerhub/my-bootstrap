package com.dap.sequence.api.dto;


import com.dap.sequence.api.dto.base.BaseRuleInfo;


/**
 * @className Rule
 * @description 规则类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class Rule {
    /**
     * 规则id
     */
    private String ruleId;
    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 规则信息
     */
    private BaseRuleInfo ruleData;

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public BaseRuleInfo getRuleData() {
        return ruleData;
    }

    public void setRuleData(BaseRuleInfo ruleData) {
        this.ruleData = ruleData;
    }

}
