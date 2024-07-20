package com.dap.sequence.client.rules;

import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.CheckRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.BaseRuleHandler;
import com.dap.sequence.client.dto.RuleParams;

import java.util.Locale;

/**
 * @className CheckRuleHandler
 * @description 校验规则测试
 * @author renle
 * @date 2023/10/24 10:20:18 
 * @version: V23.06
 */
public class CheckRuleHandler implements BaseRuleHandler {
    private static final String CHECK_FORMATTER = "[%s,%s]";

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule, RuleParams ruleParams) {
        CheckRuleInfo checkRuleInfo = (CheckRuleInfo) rule.getRuleData();
        return String.format(Locale.ENGLISH, CHECK_FORMATTER, checkRuleInfo.getCheckLength(), checkRuleInfo.getCheckPosition());
    }

    @Override
    public String getRuleName() {
        return SequenceConstant.CHECK_RULE;
    }
}
