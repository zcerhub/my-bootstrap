package com.dap.sequence.client.rules;


import com.base.api.exception.ServiceException;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.BaseRuleHandler;
import com.dap.sequence.client.dto.RuleParams;
import com.dap.sequence.client.enums.NumberCircleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @className NumberRuleHandler
 * @description 数字规则Handler
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Slf4j
public class NumberRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(SeqObj seqContext, Rule rule, RuleParams ruleParams) {
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
        Long currentData = ruleParams.getNumMap().get(rule.getRuleId()).poll();
        if (ruleParams.getOptional() && numberRuleInfo.getOptional()) {
            // 自选请求并且是自选规则 配置当前参数自选值和补位值 用于构建自选对象入库实体
            ruleParams.setOptionalValue(currentData);
            if (ruleParams.getPadMaps() != null) {
                ruleParams.setPadValue(ruleParams.getPadMaps().get(currentData));
            }
        }
        if (ruleParams.getOptional()) {
            // 自选默认左补0
            String num = String.valueOf(currentData);
            return StringUtils.leftPad(num, numberRuleInfo.getNumberEnd().toString().length(), "0");
        } else if (StringUtils.isNotBlank(numberRuleInfo.getNumberFormat())) {
            DecimalFormat integerNumber = new DecimalFormat();
            integerNumber.applyPattern(numberRuleInfo.getNumberFormat());
            return integerNumber.format(currentData);
        }
        return currentData;
    }

    public static void main(String[] args) {
        DecimalFormat integerNumber = new DecimalFormat();
        integerNumber.applyPattern("0000");
        Long currentData = 1L;
        System.out.println(integerNumber.format(currentData));
    }
    @Override
    public String getRuleName() {
        return SequenceConstant.NUMBER_RULE;
    }

    public static long getSeqNumberCurrentData(NumberRuleInfo numberRuleInfo, Long currentData, SeqObj seqContext, Rule rule) {
        long result = currentData;
        if (currentData > numberRuleInfo.getNumberEnd()) {
            switch (NumberCircleEnum.sign(numberRuleInfo.getNumberCircle())) {
                case CONTINUING_TO_GROW:
                    break;
                case STARTING_FROM_SCRATCH:
                    result = numberRuleInfo.getNumberStart();
                    break;
                case MAXIMUM_VALUE:
                    result = numberRuleInfo.getNumberEnd();
                    break;
                case THROW_AN_EXCEPTION:
                    log.error("序列[{}]第[{}]规则<{}>达到最大值", seqContext.getSequenceCode(), getSort(seqContext, rule), rule.getRuleName());
                    throw new ServiceException("序列达到最大值");
                default:
                    log.info("序列超过最大值：{}", numberRuleInfo.getNumberEnd());
            }
        }
        return result;
    }

    private static Integer getSort(SeqObj seqContext, Rule rule) {
        List<Rule> rules = seqContext.getRuleInfos();
        return IntStream.range(0, rules.size()).filter(i -> Objects.equals(rules.get(i).getRuleId(), rule.getRuleId())).findFirst().orElse(-2) + 1;
    }
}
