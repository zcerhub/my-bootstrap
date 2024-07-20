package com.dap.sequence.server.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;

import java.util.LinkedList;

/**
 * @className EnumRuleHandler
 * @description 枚举规则Handler
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class EnumRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule,  RuleParams ruleParams) {
        EnumRuleInfo enumRuleInfo = (EnumRuleInfo) rule.getRuleData();

        int index = enumRuleInfo.getIndex();
        if ((enumRuleInfo.getIndex() + enumRuleInfo.getNumberStep()) > (enumRuleInfo.getEnumStore().size())) {
            index = (enumRuleInfo.getIndex() + enumRuleInfo.getNumberStep() - enumRuleInfo.getEnumStore().size()) % enumRuleInfo.getEnumStore().size();
        } else {
            index = index + enumRuleInfo.getNumberStep();
        }
        enumRuleInfo.setIndex(index);
        return enumRuleInfo.getEnumStore().get(index - 1);
    }

    @Override
    public String getRuleName() {
        return SequenceConstant.ENUM_RUlE;
    }
}
