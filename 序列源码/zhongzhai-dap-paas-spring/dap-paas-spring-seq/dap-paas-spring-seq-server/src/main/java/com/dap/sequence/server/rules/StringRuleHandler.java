package com.dap.sequence.server.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.StringRuleInfo;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;

import java.util.LinkedList;

/**
 * @className StringRuleHandler
 * @description 字符串规则Handler
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class StringRuleHandler implements BaseRuleHandler {
    @Override
    public Object generateRule(SeqObj seqContext, Rule rule, RuleParams ruleParams) {
        StringRuleInfo stringRuleInfo = (StringRuleInfo) rule.getRuleData();
        return stringRuleInfo.getInitData();
    }

    @Override
    public String getRuleName() {
        return SequenceConstant.STRING_RULE;
    }
}
