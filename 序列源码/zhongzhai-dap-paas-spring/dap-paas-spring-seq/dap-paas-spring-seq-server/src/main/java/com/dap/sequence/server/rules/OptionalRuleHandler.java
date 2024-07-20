package com.dap.sequence.server.rules;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.OptionalRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


/**
 *自选号
 * @author 赵鹏举
 * @Descricption: 自选序号处理类
 */
@Slf4j
public class OptionalRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule, RuleParams ruleParams) {
        OptionalRuleInfo ruleInfo = (OptionalRuleInfo) rule.getRuleData();
        String padValue = String.valueOf(ruleParams.getPadValue());
        String seqValue = ruleParams.getSeqVal();
        if ("-1".equals(padValue)) {
            return seqValue;
        } else {
            if (SequenceConstant.OPTIONAL_PADDINDSTATEGY_DIY == ruleInfo.getPaddindStategy()) {
                padValue = padValue.substring(2, padValue.length() - 1);
            }
            return StringUtils.leftPad(padValue + seqValue, ruleInfo.getOptionalBit(), "0");
        }
    }

    @Override
    public String getRuleName() {
        return SequenceConstant.OPTIONAL_RULE;
    }
}
