package com.dap.sequence.server.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.DateRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;

import java.util.LinkedList;
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
        return SequenceConstant.DATE_RUlE;
    }
}
