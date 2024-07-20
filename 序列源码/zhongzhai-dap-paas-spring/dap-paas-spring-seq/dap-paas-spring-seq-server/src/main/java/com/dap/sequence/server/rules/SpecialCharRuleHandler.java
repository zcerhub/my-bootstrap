package com.dap.sequence.server.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SpecialCharRuleInfo;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;

/**
 * @Author: cao
 * Data: 2022/2/14 14:31
 * @Descricption: 占位符
 */
public class SpecialCharRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule, RuleParams ruleParams) {
        SpecialCharRuleInfo specialCharRuleInfo = (SpecialCharRuleInfo) rule.getRuleData();
        StringBuffer placeholderInfo = new StringBuffer("{");
        placeholderInfo.append(specialCharRuleInfo.getPlaceholderRule());
        placeholderInfo.append(",");
        placeholderInfo.append(specialCharRuleInfo.getPlaceholderRule() == 0 ? "" : specialCharRuleInfo.getPlaceholderLength() + "");
        placeholderInfo.append("}");
        return placeholderInfo.toString();
    }

    @Override
    public String getRuleName() {
        return SequenceConstant.SPECIAL_RULE;
    }
}
