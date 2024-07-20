package com.dap.sequence.client.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.BaseRuleHandler;
import com.dap.sequence.client.dto.RuleParams;

import java.util.Map;

/**
 * @className DateRuleHandler
 * @description 时间规则Handler
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class DateRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule,  RuleParams ruleParams) {
        Map<String, String> time = ruleParams.getTime();
        return time.get(rule.getRuleId());
    }

    @Override
    public String getRuleName() {
        return SequenceConstant.DATE_RULE;
    }
}
