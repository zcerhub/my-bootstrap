package com.dap.paas.console.seq.entity.base;

import com.alibaba.fastjson.JSON;

/**
 * @className Rule
 * @description 规则
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class Rule {

    private String ruleName;

    private String ruleType;

    /**
     * 对象
     */
    private BaseRuleInfo ruleData;

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

    @Override
    public String toString() {
        return "{" + "\"ruleName\":\"" +
                ruleName + '\"' +
                ",\"ruleType\":\"" +
                ruleType + '\"' +
                ",\"ruleData\":" +
                JSON.toJSONString(ruleData) +
                '}';
    }
}
