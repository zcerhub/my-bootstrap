package com.dap.sequence.client.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.BaseRuleHandler;
import com.dap.sequence.client.dto.RuleParams;
import com.dap.sequence.client.service.SeqDesignService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.Map;

/**
 * @className EnumRuleHandler
 * @description 枚举规则Handler
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class EnumRuleHandler implements BaseRuleHandler {
    @Autowired
    private SeqDesignService seqDesignService;

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule,  RuleParams ruleParams) {
        EnumRuleInfo enumRuleInfo = (EnumRuleInfo) rule.getRuleData();
        Map<String, LinkedList<Long>> enumMap = ruleParams.getEnumMap();
        if (enumMap != null && !enumMap.isEmpty()) {
            // 严格递增
            Long currentData = enumMap.get(rule.getRuleId()).poll();
            return enumRuleInfo.getEnumStore().get(currentData.intValue());
        } else {
            int index = enumRuleInfo.getIndex();
            if ((enumRuleInfo.getIndex() + enumRuleInfo.getNumberStep()) > (enumRuleInfo.getEnumStore().size())) {
                index = (enumRuleInfo.getIndex() + enumRuleInfo.getNumberStep() - enumRuleInfo.getEnumStore().size()) % enumRuleInfo.getEnumStore().size();
            } else {
                index = index + enumRuleInfo.getNumberStep();
            }
            enumRuleInfo.setIndex(index);
            return enumRuleInfo.getEnumStore().get(index - 1);
        }
    }


    @Override
    public String getRuleName() {
        return SequenceConstant.ENUM_RULE;
    }
}
